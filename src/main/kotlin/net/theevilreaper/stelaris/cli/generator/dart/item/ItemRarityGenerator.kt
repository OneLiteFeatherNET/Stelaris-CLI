package net.theevilreaper.stelaris.cli.generator.dart.item

import net.minestom.server.item.component.ItemRarity
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
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

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
                    .parameter(
                        ParameterSpec.positional("displayName").build()
                    )
                    .build()
            }
            .build()
        val enumFile = DartFile.builder("item_rarity")
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        enumFile.write(folder)

    }

    override fun getName() = "ItemRarityGenerator"
}
