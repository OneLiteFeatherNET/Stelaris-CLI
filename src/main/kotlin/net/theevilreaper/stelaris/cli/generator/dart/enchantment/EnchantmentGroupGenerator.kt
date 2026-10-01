package net.theevilreaper.stelaris.cli.generator.dart.enchantment

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
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

@AutoService(Generator::class)
@CodeGenerator(name = "EnchantmentGroupGenerator")
class EnchantmentGroupGenerator : BaseGenerator(
    packageName = "enchantment",
    className = "EnchantmentGroup"
) {

    override fun generate(outputPath: Path) {
        val enchantmentFolder = checkPackageFolder(outputPath, packageName)

        val enumClass = ClassSpec.enumClass(className)
            .keyed(className)
            .apply {
                EnchantmentGroup.entries.map { entry ->
                    enumProperty(EnumEntrySpec
                        .builder(entry.name.lowercase())
                        .parameter(EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(entry.classPart)))
                        .parameter(EnumParameterSpec.positional("%C", entry.classPart))
                        .build()
                    )
                }
            }
            .properties(*DEFAULT_PROPERTIES)
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(*DEFAULT_PARAMETERS)
                    .build()
            )
            .build()

        val classFile = DartFile.builder("enchantment_group")
            .keyedLookup(className)
            .type(enumClass)
            .doc("Represents a category of enchantments based on their primary application")
            .doc("")
            .doc("Enchantments are grouped by the type of items they can be applied to,")
            .doc("making it easier to filter and organize them by use case.")


        classFile.writeGenerated(enchantmentFolder, baseDir = outputPath.parent)
    }
}