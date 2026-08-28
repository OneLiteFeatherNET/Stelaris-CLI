package net.theevilreaper.stelaris.cli.generator.dart.item

import com.google.auto.service.AutoService
import net.minestom.server.item.component.MapPostProcessing
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
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

@AutoService(Generator::class)
@CodeGenerator(name = "MapPostProcessingGenerator")
class MapPostProcessingGenerator : BaseGenerator(
    className = "MapPostProcessing",
    packageName = "item",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val enumProperties = MapPostProcessing.entries.map { postProcessing ->
            val name = StringHelper.toLowerCamelCase(postProcessing.name)
            val displayName = StringHelper.mapDisplayName(postProcessing.name)
            EnumEntrySpec.builder(name)
                .parameter {
                    EnumParameterSpec.positional("%C", displayName)
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
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier { DartModifier.CONST }
                    .parameter(ParameterSpec.positional("displayName").build())
                    .build()
            }
            .build()

        val enumFile = DartFile.builder("map_post_processing")
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        enumFile.write(folder)
    }
}
