package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import com.google.auto.service.AutoService
import net.kyori.adventure.text.TranslatableComponent
import net.minestom.server.MinecraftServer
import net.minestom.server.item.enchant.Enchantment
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.CLASS_PROPERTIES
import net.theevilreaper.stelaris.cli.generator.dart.util.CONSTRUCTOR_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Files
import java.nio.file.Path

/**
 * The [EnchantmentGenerator] is responsible for generating the Dart enum for Minecraft enchantments.
 * It retrieves all enchantments from the Minecraft server and maps them to an enum format.
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
@AutoService(Generator::class)
@CodeGenerator(name = "EnchantmentGenerator")
class EnchantmentGenerator : BaseGenerator(
    className = "Enchantment",
    packageName = "enchantment",
) {

    override fun generate(outputPath: Path) {
        val enchantmentFolder = checkPackageFolder(outputPath, packageName)
        val enchantmentData: MutableCollection<Enchantment> = MinecraftServer.getEnchantmentRegistry().values()
        val mappedEnchantments: Map<EnchantmentGroup, List<Enchantment>> = enchantmentData.mapNotNull { enchantment ->
            val key = enchantment.supportedItems().key()?.key()?.asString() ?: EMPTY_STRING
            val group = EnchantmentGroup.matchGroup(key)
            if (group == null) {
                null  // ignore this enchantment in the grouping
            } else {
                group to enchantment
            }
        }.groupBy(
            keySelector = { it.first },   // group = it.first
            valueTransform = { it.second } // enchantment = it.second
        )

        mappedEnchantments.forEach { (group, enchantments) ->
            val properties = enchantments
                .map { mapEnchantmentToEnumProperty(it) }
                .distinctBy { (key, _) -> key }
                .sortedBy { (key, _) -> key }
                .map { (_, entry) -> entry }
            val updatedClassName = "${group.classPart.replaceFirstChar { it.uppercase() }}$className"

            val enumClass = ClassSpec.enumClass(updatedClassName)
                .implements(ClassName("Enchantment"))
                .enumProperties(*properties.toTypedArray())
                .properties(*CLASS_PROPERTIES)
                .constructor {
                    ConstructorSpec.builder(updatedClassName)
                        .modifier(DartModifier.CONST)
                        .parameters(*CONSTRUCTOR_PARAMETERS)
                        .build()
                }
                .build()
            val fileName = "${group.classPart}_${className.replaceFirstChar { it.lowercase() }}"
            val enumFile = DartFile.builder(fileName)
                .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../api/enchantment.dart"))
                .type(enumClass)
            enumFile.writeGenerated(enchantmentFolder, baseDir = outputPath)
        }
    }

    /**
     * Maps the [Enchantment] to an [EnumEntrySpec] which can be used in the generated enum.
     * @param enchantment the enchantment to map
     * @return the key of the enchantment together with the mapped [EnumEntrySpec]
     */
    private fun mapEnchantmentToEnumProperty(enchantment: Enchantment): Pair<String, EnumEntrySpec> {
        val translatable = enchantment.description() as? TranslatableComponent
        val keyString = translatable?.key() ?: EMPTY_STRING
        val key = keyString.split(".").drop(1).joinToString(":")
        val rawName = keyString.substringAfterLast(".")
        val entry = EnumEntrySpec.builder(StringHelper.toLowerCamelCase(rawName))
            .parameter(EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(rawName)))
            .parameter(EnumParameterSpec.positional("%C", key))
            .parameter(EnumParameterSpec.positional("%L", enchantment.maxLevel()))
            .build()
        return key to entry
    }
}