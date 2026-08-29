package net.theevilreaper.stelaris.cli.generator.dart.item

import com.google.auto.service.AutoService
import net.minestom.server.item.component.FireworkExplosion
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
@CodeGenerator(name = "FireworkShapeGenerator")
class FireworkShapeGenerator : BaseGenerator(
    className = "FireworkShape",
    packageName = "item",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val enumProperties = FireworkExplosion.Shape.entries.mapIndexed { index, shape ->
            val name = StringHelper.toLowerCamelCase(shape.name)
            val displayName = StringHelper.mapDisplayName(shape.name)
            EnumEntrySpec.builder(name)
                .parameter {
                    EnumParameterSpec.positional("%C", displayName)
                }
                .parameter {
                    EnumParameterSpec.positional("%L", index)
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

        val enumFile = DartFile.builder("firework_shape")
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        enumFile.write(folder)
    }
}
