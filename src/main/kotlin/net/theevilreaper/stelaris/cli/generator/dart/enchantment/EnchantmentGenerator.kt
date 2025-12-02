package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import net.kyori.adventure.text.TranslatableComponent
import net.minestom.server.MinecraftServer
import net.minestom.server.item.enchant.Enchantment
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.InheritKeyword
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.util.CLASS_PROPERTIES
import net.theevilreaper.stelaris.cli.generator.dart.util.CONSTRUCTOR_PARAMETERS
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import net.theevilreaper.stelaris.cli.util.StringHelper
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
                .function {
                    // Create a function to make a lookup
                    val returnType = ClassName(updatedClassName, isNullable = true)
                    FunctionSpec.builder("fromValue")
                        .modifier { DartModifier.STATIC }
                        .doc("Tries to find the corresponding enchantment type based on a given input")
                        .doc("If the enchantment string doesn't, it returns null.")
                        .doc("")
                        .doc("The [input] to retrieve the enchantment reference for")
                        .doc("Returns the matched [%L] or null if no enchantment could be found", updatedClassName)
                        .returns(returnType)
                        .parameter {
                            ParameterSpec.positional("input", String::class)
                                .build()
                        }
                        .addCode(
                            buildCodeBlock {
                                beginControlFlow("for (var enchantment in values)")
                                addStatement("if (enchantment.minecraftValue == input) return enchantment;")
                                endControlFlow()
                                add("return null;")
                            }

                        )
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
        val key = (enchantment.description() as TranslatableComponent).key()

        val enumName = extractEnumName(key)
        val minecraftId = extractMinecraftId(key)
        val displayName = StringHelper.mapDisplayName(enumName)

        return EnumEntrySpec.builder(enumName)
            .parameter(EnumParameterSpec.positional("%C", displayName))
            .parameter(EnumParameterSpec.positional("%C", minecraftId))
            .parameter(EnumParameterSpec.positional("%L", enchantment.maxLevel()))
            .build()
    }

    /**
     * Converts a Minecraft translation key into a camelCase enum name.
     *
     * Takes a key like "enchantment.minecraft.depth_strider" and transforms it
     * into "depthStrider" by taking the last segment, splitting on underscores,
     * and capitalizing each word except the first.
     *
     * @param translationKey the full translation key from Minecraft
     * @return a camelCase identifier suitable for Dart enum names
     */
    private fun extractEnumName(translationKey: String): String {
        return translationKey
            .substringAfterLast(".")
            .split("_")
            .mapIndexed { index, part ->
                if (index == 0) part.lowercase()
                else part.replaceFirstChar { it.uppercase() }
            }
            .joinToString("")
    }

    /**
     * Extracts the Minecraft resource identifier from a translation key.
     *
     * Strips the "enchantment" prefix and rebuilds the remaining parts with colons,
     * turning "enchantment.minecraft.depth_strider" into "minecraft:depth_strider".
     *
     * @param translationKey the full translation key from Minecraft
     * @return the namespaced identifier used by Minecraft internally
     */
    private fun extractMinecraftId(translationKey: String): String {
        return translationKey
            .split(".")
            .drop(1)
            .joinToString(":")
    }

    /**
     * Returns the name of the generator.
     * @return the name
     */
    override fun getName() = "EnchantmentGenerator"
}