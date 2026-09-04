package net.theevilreaper.stelaris.cli.generator.dart.color

import com.google.auto.service.AutoService
import net.kyori.adventure.text.format.NamedTextColor
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

/**
 * Generator for the [NamedTextColor] enum in Dart.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "NamedTextColorGenerator")
class NamedTextColorGenerator : BaseGenerator(
    className = "NamedTextColor",
    packageName = "color",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val enumProperties = NamedTextColor.NAMES.values()
            .sortedBy { it.toString() }
            .map { namedColor ->
            val rawName = namedColor.toString()
            val variableName = StringHelper.toLowerCamelCase(rawName)
            val displayName = StringHelper.mapDisplayName(rawName)

            EnumEntrySpec.builder(variableName)
                .parameter {
                    EnumParameterSpec.positional("%C", displayName)
                }
                .parameter {
                    EnumParameterSpec.positional("%C", rawName)
                }
                .parameter {
                    EnumParameterSpec.positional("%L", formatColor(namedColor.value()))
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
                PropertySpec.builder("name", String::class)
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .property {
                PropertySpec.builder("color", ClassName("Color"))
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier { DartModifier.CONST }
                    .parameter(ParameterSpec.positional("displayName").build())
                    .parameter(ParameterSpec.positional("name").build())
                    .parameter(ParameterSpec.positional("color").build())
                    .build()
            }
            .build()

        val enumFile = DartFile.builder("named_text_color")
            .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../api/color.dart"))
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        enumFile.write(folder, baseDir = outputPath)
    }

    private fun formatColor(rgb: Int): String {
        return "Color.fromRGB(0x%06x)".format(rgb)
    }
}
