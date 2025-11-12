package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import com.google.common.collect.ImmutableList
import net.kyori.adventure.text.TranslatableComponent
import net.minestom.server.MinecraftServer
import net.minestom.server.item.enchant.Enchantment
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.InheritKeyword
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.util.CLASS_PROPERTIES
import net.theevilreaper.stelaris.cli.generator.dart.util.CONSTRUCTOR_PARAMETERS
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
class EnchantmentGenerator : BaseGenerator(
    className = "Enchantment",
    packageName = "enchantment",
) {

    override fun generate(javaPath: Path) {
        val enchantmentFolder = javaPath.resolve(packageName)
        checkPackageFolder(javaPath, packageName)
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
            val properties = mutableSetOf<EnumEntrySpec>()
            enchantments.forEach { properties.add(mapEnchantmentToEnumProperty(it)) }
            val updatedClassName = "${group.classPart.replaceFirstChar { it.uppercase() }}$className"

            val enumClass = ClassSpec.enumClass(updatedClassName)
                .superClass(ClassName("Enchantment"), InheritKeyword.IMPLEMENTS)
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
                .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../api/enchantment"))
                .doc("The file is generated. Don't change anything here")
                .type(enumClass)
                .build()
            enumFile.write(enchantmentFolder)
        }
    }

    /**
     * Maps the [Enchantment] to an [EnumEntrySpec] which can be used in the generated enum.
     * @param enchantment the enchantment to map
     * @return the mapped [EnumEntrySpec]
     */
    private fun mapEnchantmentToEnumProperty(enchantment: Enchantment): EnumEntrySpec {
        val translatable = enchantment.description() as TranslatableComponent
        val minecraftValue = translatable.key().split(".").drop(1).joinToString(":")
        val enchantmentName = translatable.key().substringAfterLast(".").split("_")
            .mapIndexed { index, part ->
                if (index == 0) part.lowercase() else part.replaceFirstChar { it.uppercase() }
            }
            .joinToString("")
        return EnumEntrySpec.builder(enchantmentName)
            .parameter(
                EnumParameterSpec.positional(
                    "%C",
                    StringHelper.mapDisplayName(enchantmentName)
                )
            )
            .parameter(EnumParameterSpec.positional("%S", minecraftValue))
            .parameter(EnumParameterSpec.positional("%L", enchantment.maxLevel()))
            .build()
    }

    /**
     * Returns the name of the generator.
     * @return the name
     */
    override fun getName() = "EnchantmentGenerator"
}