package net.theevilreaper.stelaris.cli.generator.dart

import net.kyori.adventure.text.TranslatableComponent
import net.minestom.server.MinecraftServer
import net.minestom.server.item.enchant.Enchantment
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.util.CLASS_PROPERTIES
import net.theevilreaper.stelaris.cli.generator.dart.util.CONSTRUCTOR_PARAMETERS
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

    private val defaultLevel: Int = 1

    override fun generate(javaPath: Path) {
        val enchantmentData: MutableCollection<Enchantment> = MinecraftServer.getEnchantmentRegistry().values()
        val properties = mutableSetOf<EnumEntrySpec>()

        enchantmentData.forEach { enchantment ->
            properties.add(mapEnchantmentToEnumProperty(enchantment))
        }

        val enumClass = ClassSpec.enumClass(className)
            .enumProperties(*properties.toTypedArray())
            .properties(*CLASS_PROPERTIES)
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(*CONSTRUCTOR_PARAMETERS)
                    .build()
            }
            .build()
        val enumFile = DartFile.builder(className.replaceFirstChar { it.lowercase() })
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        enumFile.write(javaPath)
    }

    /**
     * Maps the [Enchantment] to an [EnumEntrySpec] which can be used in the generated enum.
     * @param enchantment the enchantment to map
     * @return the mapped [EnumEntrySpec]
     */
    private fun mapEnchantmentToEnumProperty(enchantment: Enchantment): EnumEntrySpec {
        val translatable = enchantment.description() as TranslatableComponent
        val enchantmentName = translatable.key().substringAfterLast(".")
        return EnumEntrySpec.builder(enchantmentName)
            .parameter(
                EnumParameterSpec.positional(
                    "%C",
                    StringHelper.mapDisplayName(enchantmentName)
                )
            )
            .parameter(EnumParameterSpec.positional("%L", defaultLevel))
            .parameter(EnumParameterSpec.positional("%L", enchantment.maxLevel()))
            .build()
    }

    /**
     * Returns the name of the generator.
     * @return the name
     */
    override fun getName() = "EnchantmentGenerator"

    /**
     * Returns whether the generator is experimental or not.
     * @return false, since this generator is not experimental
     */
    override fun isExperimental(): Boolean = false
}
