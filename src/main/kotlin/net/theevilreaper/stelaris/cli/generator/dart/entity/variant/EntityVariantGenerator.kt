package net.theevilreaper.stelaris.cli.generator.dart.entity.variant

import com.google.auto.service.AutoService
import net.minestom.server.entity.metadata.animal.FoxMeta
import net.minestom.server.entity.metadata.animal.MooshroomMeta
import net.minestom.server.entity.metadata.animal.RabbitMeta
import net.minestom.server.entity.metadata.animal.tameable.ParrotMeta
import net.minestom.server.entity.metadata.water.AxolotlMeta
import net.minestom.server.entity.metadata.water.fish.SalmonMeta
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartFileBuilder
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.generator.dart.util.keyed
import net.theevilreaper.stelaris.cli.generator.dart.util.keyedLookup
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

@AutoService(Generator::class)
@CodeGenerator(name = "EntityVariantGenerator")
class EntityVariantGenerator : BaseGenerator(
    className = "EntityVariant",
    packageName = "entity/variant",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val files = listOf(
            generateEnum("AxolotlVariant", "axolotl_variant", AxolotlMeta.Variant.entries.map { it.name }),
            generateEnum("FoxVariant", "fox_variant", FoxMeta.Variant.entries.map { it.name }),
            generateEnum("MooshroomVariant", "mooshroom_variant", MooshroomMeta.Variant.entries.map { it.name }, withId = false),
            generateEnum("ParrotVariant", "parrot_variant", ParrotMeta.Color.entries.map { it.name }),
            generateEnum("RabbitVariant", "rabbit_variant", RabbitMeta.Variant.entries.map { it.name.replace("THE_", "") }),
            generateEnum("SalmonSize", "salmon_size", SalmonMeta.Size.entries.map { it.name }),
        )
        files.forEach { it.writeGenerated(folder, baseDir = outputPath.parent) }
    }

    /**
     * Generates the enum for the given variant names. The key of an entry is its lowercase name.
     * @param className the name of the enum
     * @param fileName the name of the file
     * @param names the names of the variants in the order of the game
     * @param withId true if the index of a variant should be added as its id
     * @return the [DartFileBuilder] of the file
     */
    private fun generateEnum(
        className: String,
        fileName: String,
        names: List<String>,
        withId: Boolean = true,
    ): DartFileBuilder {
        val enumProperties = names.mapIndexed { index, name ->
            EnumEntrySpec.builder(StringHelper.toLowerCamelCase(name))
                .parameter { EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(name)) }
                .parameter { EnumParameterSpec.positional("%C", name.lowercase()) }
                .apply { if (withId) parameter { EnumParameterSpec.positional("%L", index) } }
                .build()
        }

        val enumClass = ClassSpec.enumClass(className)
            .keyed(className)
            .enumProperties(*enumProperties.toTypedArray())
            .properties(*DEFAULT_PROPERTIES)
            .apply { if (withId) property(PropertySpec.builder("id", Int::class).modifier(DartModifier.FINAL).build()) }
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(*DEFAULT_PARAMETERS)
                    .apply { if (withId) parameter(ParameterSpec.positional("id").build()) }
                    .build()
            )
            .build()

        return DartFile.builder(fileName)
            .keyedLookup(className, folderDepth = packageName.split('/').size)
            .type(enumClass)
    }
}
