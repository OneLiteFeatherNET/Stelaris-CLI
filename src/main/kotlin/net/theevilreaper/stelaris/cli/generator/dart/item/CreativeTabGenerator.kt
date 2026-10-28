package net.theevilreaper.stelaris.cli.generator.dart.item

import com.google.auto.service.AutoService
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.generator.dart.util.keyed
import net.theevilreaper.stelaris.cli.generator.dart.util.keyedLookup
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Path

@AutoService(Generator::class)
@CodeGenerator(name = "CreativeTabGenerator")
class CreativeTabGenerator : BaseGenerator(
    className = "CreativeTab",
    packageName = "item",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val enumProperties = CreativeTab.entries.map {
            EnumEntrySpec.builder(it.name.lowercase())
                .parameter(EnumParameterSpec.positional("%C", it.displayName))
                .parameter(EnumParameterSpec.positional("%C", it.key))
                .build()
        }
        val enumClass = ClassSpec.enumClass(className)
            .keyed(className)
            .enumProperties(*enumProperties.toTypedArray())
            .properties(*DEFAULT_PROPERTIES)
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(*DEFAULT_PARAMETERS)
                    .build()
            )
            .build()
        val enumFile = DartFile.builder("creative_tab")
            .keyedLookup(className)
            .type(enumClass)
            .doc("Represents the item groups of the creative inventory")
        enumFile.writeGenerated(folder, baseDir = outputPath.parent)
    }
}
