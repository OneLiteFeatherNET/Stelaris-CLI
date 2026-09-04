package net.theevilreaper.stelaris.cli.generator.dart.text

import com.google.auto.service.AutoService
import net.kyori.adventure.text.event.HoverEvent
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

/**
 * Generator for the [HoverEvent.Action] enum in Dart.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "HoverEventActionGenerator")
class HoverEventActionGenerator : BaseGenerator(
    className = "HoverEventAction",
    packageName = "text",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val enumProperties = HoverEvent.Action.NAMES.values()
            .filter { it != HoverEvent.Action.SHOW_ACHIEVEMENT }
            .sortedBy { it.name() }
            .map { action ->
                val rawName = action.name()
                val variableName = StringHelper.toLowerCamelCase(rawName)
                val displayName = StringHelper.mapDisplayName(rawName)

                EnumEntrySpec.builder(variableName)
                    .parameter {
                        EnumParameterSpec.positional("%C", displayName)
                    }
                    .parameter {
                        EnumParameterSpec.positional("%C", rawName)
                    }
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
                PropertySpec.builder("name", String::class)
                    .modifier { DartModifier.FINAL }
                    .build()
            }
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier { DartModifier.CONST }
                    .parameter(ParameterSpec.positional("displayName").build())
                    .parameter(ParameterSpec.positional("name").build())
                    .build()
            }
            .build()

        val enumFile = DartFile.builder("hover_event_action")
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        enumFile.write(folder, baseDir = outputPath)
    }
}
