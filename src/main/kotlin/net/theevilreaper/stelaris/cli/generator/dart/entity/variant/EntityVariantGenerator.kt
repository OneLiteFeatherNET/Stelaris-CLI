package net.theevilreaper.stelaris.cli.generator.dart.entity.variant

import net.minestom.server.entity.metadata.animal.FoxMeta
import net.minestom.server.entity.metadata.animal.MooshroomMeta
import net.minestom.server.entity.metadata.animal.RabbitMeta
import net.minestom.server.entity.metadata.animal.tameable.ParrotMeta
import net.minestom.server.entity.metadata.water.AxolotlMeta
import net.minestom.server.entity.metadata.water.fish.SalmonMeta
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

class EntityVariantGenerator : BaseGenerator(
    className = "EntityVariant",
    packageName = "entity/variant",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val files = mutableListOf<DartFile>()

        // 1. AxolotlVariant
        files.add(generateIdEnum(
            className = "AxolotlVariant",
            fileName = "axolotl_variant",
            entries = AxolotlMeta.Variant.entries.mapIndexed { index, v ->
                StringHelper.toLowerCamelCase(v.name) to (StringHelper.mapDisplayName(v.name) to index)
            }
        ))

        // 2. FoxVariant
        files.add(generateIdEnum(
            className = "FoxVariant",
            fileName = "fox_variant",
            entries = FoxMeta.Variant.entries.mapIndexed { index, v ->
                StringHelper.toLowerCamelCase(v.name) to (StringHelper.mapDisplayName(v.name) to index)
            }
        ))

        // 3. MooshroomVariant
        files.add(generateStringEnum(
            className = "MooshroomVariant",
            fileName = "mooshroom_variant",
            entries = MooshroomMeta.Variant.entries.map { v ->
                StringHelper.toLowerCamelCase(v.name) to (StringHelper.mapDisplayName(v.name) to v.name.lowercase())
            }
        ))

        // 4. ParrotVariant
        files.add(generateIdEnum(
            className = "ParrotVariant",
            fileName = "parrot_variant",
            entries = ParrotMeta.Color.entries.mapIndexed { index, v ->
                StringHelper.toLowerCamelCase(v.name) to (StringHelper.mapDisplayName(v.name) to index)
            }
        ))

        // 5. RabbitVariant
        files.add(generateIdEnum(
            className = "RabbitVariant",
            fileName = "rabbit_variant",
            entries = RabbitMeta.Variant.entries.mapIndexed { index, v ->
                val name = StringHelper.toLowerCamelCase(v.name.replace("THE_", ""))
                name to (StringHelper.mapDisplayName(v.name.replace("THE_", "")) to index)
            }
        ))

        // 6. SalmonSize
        files.add(generateIdEnum(
            className = "SalmonSize",
            fileName = "salmon_size",
            entries = SalmonMeta.Size.entries.mapIndexed { index, v ->
                StringHelper.toLowerCamelCase(v.name) to (StringHelper.mapDisplayName(v.name) to index)
            }
        ))

        files.forEach { it.write(folder) }
    }

    private fun generateIdEnum(
        className: String,
        fileName: String,
        entries: List<Pair<String, Pair<String, Int>>>,
    ): DartFile {
        val enumProperties = entries.map { (name, data) ->
            val (displayName, id) = data
            EnumEntrySpec.builder(name)
                .parameter { EnumParameterSpec.positional("%C", displayName) }
                .parameter { EnumParameterSpec.positional("%L", id) }
                .build()
        }

        val enumClass = ClassSpec.enumClass(className)
            .enumProperties(*enumProperties.toTypedArray())
            .property {
                PropertySpec.builder("displayName", String::class)
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .property {
                PropertySpec.builder("id", Int::class)
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier { DartModifier.CONST }
                    .parameter(ParameterSpec.positional("displayName").build())
                    .parameter(ParameterSpec.positional("id").build())
                    .build()
            }
            .build()

        return DartFile.builder(fileName)
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
    }

    private fun generateStringEnum(
        className: String,
        fileName: String,
        entries: List<Pair<String, Pair<String, String>>>,
    ): DartFile {
        val enumProperties = entries.map { (name, data) ->
            val (displayName, key) = data
            EnumEntrySpec.builder(name)
                .parameter { EnumParameterSpec.positional("%C", displayName) }
                .parameter { EnumParameterSpec.positional("%C", key) }
                .build()
        }

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

        return DartFile.builder(fileName)
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
    }

    override fun getName() = "EntityVariantGenerator"
}
