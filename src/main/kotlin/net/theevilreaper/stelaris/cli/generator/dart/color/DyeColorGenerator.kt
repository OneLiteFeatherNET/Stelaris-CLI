package net.theevilreaper.stelaris.cli.generator.dart.color

import com.google.auto.service.AutoService
import net.minestom.server.color.DyeColor
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.generator.dart.util.apiImport
import net.theevilreaper.stelaris.cli.generator.dart.util.keyed
import net.theevilreaper.stelaris.cli.generator.dart.util.keyedLookup
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

@AutoService(Generator::class)
@CodeGenerator(name = "DyeColorGenerator")
class DyeColorGenerator : BaseGenerator(
    className = "DyeColor",
    packageName = "color",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val enumProperties = DyeColor.entries.map { dyeColor ->
            val name = StringHelper.toLowerCamelCase(dyeColor.name)
            EnumEntrySpec.builder(name)
                .parameter {
                    EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(dyeColor.name))
                }
                .parameter {
                    EnumParameterSpec.positional("%C", dyeColor.name.lowercase())
                }
                .parameter {
                    EnumParameterSpec.positional("%L", formatColor(dyeColor.color().asRGB()))
                }
                .parameter {
                    EnumParameterSpec.positional("%L", formatColor(dyeColor.textColor().asRGB()))
                }
                .parameter {
                    EnumParameterSpec.positional("%L", formatColor(dyeColor.fireworkColor().asRGB()))
                }
                .parameter {
                    EnumParameterSpec.positional("%L", dyeColor.mapColorId())
                }
                .build()
        }.toList()

        val enumClass = ClassSpec.enumClass(className)
            .keyed(className)
            .enumProperties(*enumProperties.toTypedArray())
            .properties(*DEFAULT_PROPERTIES)
            .property {
                PropertySpec.builder("textureDiffuseColor", ClassName("RgbColor"))
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .property {
                PropertySpec.builder("textColor", ClassName("RgbColor"))
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .property {
                PropertySpec.builder("fireworkColor", ClassName("RgbColor"))
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .property {
                PropertySpec.builder("mapColorId", Int::class)
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier { DartModifier.CONST }
                    .parameters(*DEFAULT_PARAMETERS)
                    .parameter(ParameterSpec.positional("textureDiffuseColor").build())
                    .parameter(ParameterSpec.positional("textColor").build())
                    .parameter(ParameterSpec.positional("fireworkColor").build())
                    .parameter(ParameterSpec.positional("mapColorId").build())
                    .build()
            }
            .build()

        val enumFile = DartFile.builder("dye_color")
            .keyedLookup(className)
            .directive(apiImport("rgb_color.dart"))
            .type(enumClass)
        enumFile.writeGenerated(folder, baseDir = outputPath.parent)
    }

    private fun formatColor(rgb: Int): String {
        return "RgbColor.fromRGB(0x%06x)".format(rgb)
    }
}
