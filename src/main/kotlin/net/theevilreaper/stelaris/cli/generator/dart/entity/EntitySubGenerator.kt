package net.theevilreaper.stelaris.cli.generator.dart.entity

import net.minestom.server.entity.EntityType
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassBuilder
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import net.theevilreaper.stelaris.cli.util.StringHelper

internal object EntitySubGenerator {

    fun generateEntityEnum(className: String, entities: List<EntityType>): ClassBuilder {
        val enumProperties = entities.map {
            val rawName = it.name()
            val nameWithoutMinecraftPrefix = rawName.replace("minecraft:", EMPTY_STRING)
            val variableName = StringHelper.toLowerCamelCase(nameWithoutMinecraftPrefix)
            val name = StringHelper.mapDisplayName(nameWithoutMinecraftPrefix)
            EnumEntrySpec.builder(variableName)
                .parameter(EnumParameterSpec.positional("%C", name))
                .parameter(EnumParameterSpec.positional("%C", rawName))
                .build()
        }.toSet()

        val enumFile = ClassSpec.enumClass(className)
            .enumProperties(*enumProperties.toTypedArray())
            .properties(*DEFAULT_PROPERTIES)
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(*DEFAULT_PARAMETERS)
                    .build()
            )
        return enumFile
    }
}
