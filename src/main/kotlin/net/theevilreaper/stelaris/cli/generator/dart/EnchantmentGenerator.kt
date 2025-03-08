package net.theevilreaper.stelaris.cli.generator.dart

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
 * The [EnchantmentGenerator] contains the logic to map the data from an [EnchantmentWrapper] into a dart enum
 * which contains all available enchantments.
 * @constructor Sets the basic values for the generation
 * @version 1.0
 * @author theEvilReaper
 */
class EnchantmentGenerator : BaseGenerator(
    className = "Enchantment",
    packageName = "enchantment",
) {

    private val defaultLevel: Int = 1

    override fun generate(javaPath: Path) {
        val enchantmentData = MinecraftServer.getEnchantmentRegistry().values()
        val properties = mutableSetOf<EnumEntrySpec>()
        enchantmentData.forEach { properties.add(mapEnchantmentToEnumProperty(it)) }
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
     * Maps the given [EnchantmentWrapper] into a [EnumPropertySpec].
     * @param enchantment the given enchantment
     * @return the generated property
     */
    private fun mapEnchantmentToEnumProperty(enchantment: Enchantment): EnumEntrySpec {
        val enchantmentEntry = enchantment.registry()
        return EnumEntrySpec.builder(enchantmentEntry!!.namespace.path())
            .parameter(EnumParameterSpec.positional(
                "%C",
                StringHelper.mapDisplayName(enchantmentEntry.namespace.path())
            ))
            .parameter(EnumParameterSpec.positional("%C", "MEEPO"))
            .parameter(EnumParameterSpec.positional("%L", defaultLevel))
            .parameter(EnumParameterSpec.positional("%L", 1)) //TODO: Fix me
            .build()
    }

    /**
     * Returns the name from [EnchantmentWrapper] which is used as identifier.
     * @return the given name
     */
    override fun getName() = "EnchantmentGenerator"
}
