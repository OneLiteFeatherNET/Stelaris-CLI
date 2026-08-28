package net.theevilreaper.stelaris.cli.generator.dart.villager

import net.minestom.server.entity.VillagerType
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

class VillagerTypeGenerator : BaseGenerator(
    className = "VillagerType",
    packageName = "villager",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val enumProperties = VillagerType.entries.map { villagerType ->
            val name = StringHelper.toLowerCamelCase(villagerType.name)
            val displayName = StringHelper.mapDisplayName(villagerType.name)
            val key = "minecraft:${villagerType.name.lowercase()}"
            EnumEntrySpec.builder(name)
                .parameter {
                    EnumParameterSpec.positional("%C", displayName)
                }
                .parameter {
                    EnumParameterSpec.positional("%C", key)
                }
                .build()
        }.toList()

        val enumClass = ClassSpec.enumClass(className)
            .enumProperties(*enumProperties.toTypedArray())
            .property {
                PropertySpec.builder("displayName", String::class)
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .property {
                PropertySpec.builder("key", String::class)
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier { DartModifier.CONST }
                    .parameter(ParameterSpec.positional("displayName").build())
                    .parameter(ParameterSpec.positional("key").build())
                    .build()
            }
            .build()

        val enumFile = DartFile.builder("villager_type")
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        enumFile.write(folder)
    }

    override fun getName() = "VillagerTypeGenerator"
}
