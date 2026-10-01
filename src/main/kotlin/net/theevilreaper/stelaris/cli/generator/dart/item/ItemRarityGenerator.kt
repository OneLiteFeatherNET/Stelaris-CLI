package net.theevilreaper.stelaris.cli.generator.dart.item

import com.google.auto.service.AutoService
import net.minestom.server.item.component.ItemRarity
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
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
@CodeGenerator(name = "ItemRarityGenerator")
class ItemRarityGenerator : BaseGenerator(
    className = "ItemRarity",
    packageName = "item",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val rarities = ItemRarity.entries
        val enumProperties = rarities.map {
            val name = it.name.lowercase()
            EnumEntrySpec.builder(name)
                .parameter {
                    EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(name))
                }
                .parameter {
                    EnumParameterSpec.positional("%C", name)
                }
                .build()
        }.toList()
        val enumClass = ClassSpec.enumClass(className)
            .keyed(className)
            .enumProperties(*enumProperties.toTypedArray())
            .properties(*DEFAULT_PROPERTIES)
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier { DartModifier.CONST }
                    .parameters(*DEFAULT_PARAMETERS)
                    .build()
            }
            .build()
        val enumFile = DartFile.builder("item_rarity")
            .keyedLookup(className)
            .type(enumClass)
        enumFile.writeGenerated(folder, baseDir = outputPath.parent)

    }
}
