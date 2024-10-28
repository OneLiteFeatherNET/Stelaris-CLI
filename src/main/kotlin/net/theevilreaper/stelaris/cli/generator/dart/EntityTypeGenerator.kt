package net.theevilreaper.stelaris.cli.generator.dart

import net.minestom.server.entity.EntityType
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

class EntityTypeGenerator : BaseGenerator<EntityType>(
    className = "EntityType",
    packageName = "entity_type",
) {

    override fun generate(javaPath: Path) {
        val models = EntityType.values()
        val enumEntries = mutableListOf<EnumEntrySpec>()
        for (type in models) {
            val name = type.name().replace("minecraft:", EMPTY_STRING)
            enumEntries.add(
                EnumEntrySpec.builder(name.lowercase())
                    .parameter(EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(name)))
                    .parameter(EnumParameterSpec.positional("%C", name))
                    .build()
            )
        }
        val enumClass = ClassSpec.enumClass(className)
            .properties(*DEFAULT_PROPERTIES)
            .enumProperties(*enumEntries.toTypedArray())
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(*DEFAULT_PARAMETERS)
                    .build()
            )
            .build()

        val file = DartFile.builder(packageName)
            .doc("Generated class to represent the available entities from the game Minecraft")
            .type(enumClass)
            .build()
        file.write(javaPath)
    }

    override fun getName() = "EntityTypeGenerator"
}