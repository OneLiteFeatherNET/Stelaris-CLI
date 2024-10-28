package net.theevilreaper.stelaris.cli.generator.dart.material

import net.minestom.server.item.Material
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassBuilder
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import net.theevilreaper.stelaris.cli.util.StringHelper

internal object MaterialSubGenerator {

    private const val MATERIAL_KEY = "material"
    private const val MAX_STACK_SIZE = "maxStackSize"
    private val enumModifier = DartModifier.FINAL

    fun generateBlockMaterialEnum(className: String, materials: List<Material>): ClassBuilder {
        val enumProperties = materials.map {
            val name = escapeMinecraftPart(it.name())
            EnumEntrySpec.builder(name)
                .parameter(EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(name)))
                .parameter(EnumParameterSpec.positional("%C", name))
                .parameter(EnumParameterSpec.positional("%L", it.maxStackSize()))
                .build()
        }.toSet()
        val enumFile = ClassSpec.enumClass(className)
            .enumProperties(*enumProperties.toTypedArray())
            .properties(
                PropertySpec.builder("displayName", String::class).modifier(enumModifier).build(),
                PropertySpec.builder(MATERIAL_KEY, String::class).modifier(enumModifier).build(),
                PropertySpec.builder(MAX_STACK_SIZE, Int::class).modifier(enumModifier).build()
            )
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(
                        ParameterSpec.positional("displayName").build(),
                        ParameterSpec.positional(MATERIAL_KEY).build(),
                        ParameterSpec.positional(MAX_STACK_SIZE).build()
                    )
                    .build()
            )
        return enumFile
    }

    /**
     * Escapes the minecraft part from the given name.
     * @param name the name to escape
     * @return the escaped name
     */
    private fun escapeMinecraftPart(name: String): String {
        return name.replace("minecraft:", EMPTY_STRING)
    }
}