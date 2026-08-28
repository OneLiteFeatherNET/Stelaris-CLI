package net.theevilreaper.stelaris.cli.generator.dart.painting

import com.google.auto.service.AutoService
import net.minestom.server.entity.metadata.other.PaintingVariant
import net.theevilreaper.dartpoet.DartFile
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
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

/**
 * Generates the [PaintingVariant] enum for dart.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
@AutoService(Generator::class)
@CodeGenerator(name = "PaintingVariantGenerator")
class PaintingVariantGenerator : BaseGenerator(
    className = "PaintingVariant",
    packageName = "painting",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val registry = PaintingVariant.createDefaultRegistry()
        val enumEntries = mutableListOf<EnumEntrySpec>()

        val sortedEntries = registry.values().sortedBy { registry.getKey(it)?.name() ?: EMPTY_STRING }

        for (variant in sortedEntries) {
            val key = registry.getKey(variant)?.name() ?: continue
            val nameWithoutPrefix = key.replace("minecraft:", EMPTY_STRING)
            val variableName = StringHelper.toLowerCamelCase(nameWithoutPrefix)
            val displayName = StringHelper.mapDisplayName(nameWithoutPrefix)

            enumEntries.add(
                EnumEntrySpec.builder(variableName)
                    .parameter(EnumParameterSpec.positional("%C", displayName))
                    .parameter(EnumParameterSpec.positional("%C", key))
                    .parameter(EnumParameterSpec.positional("%L", variant.width()))
                    .parameter(EnumParameterSpec.positional("%L", variant.height()))
                    .parameter(EnumParameterSpec.positional("%C", variant.assetId().asString()))
                    .build()
            )
        }

        val enumClass = ClassSpec.enumClass(className)
            .enumProperties(*enumEntries.toTypedArray())
            .properties(
                PropertySpec.builder("displayName", String::class).modifier(DartModifier.FINAL).build(),
                PropertySpec.builder("key", String::class).modifier(DartModifier.FINAL).build(),
                PropertySpec.builder("width", Int::class).modifier(DartModifier.FINAL).build(),
                PropertySpec.builder("height", Int::class).modifier(DartModifier.FINAL).build(),
                PropertySpec.builder("assetId", String::class).modifier(DartModifier.FINAL).build()
            )
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(
                        ParameterSpec.positional("displayName").build(),
                        ParameterSpec.positional("key").build(),
                        ParameterSpec.positional("width").build(),
                        ParameterSpec.positional("height").build(),
                        ParameterSpec.positional("assetId").build()
                    )
                    .build()
            )
            .build()

        val file = DartFile.builder("painting_variant")
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        file.write(folder)
    }
}
