package net.theevilreaper.stelaris.cli.generator.dart

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.minestom.server.item.ItemHideFlag
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

class ItemFlagGenerator : BaseGenerator<ItemHideFlag>(
    className = "ItemFlags",
    packageName = "item_flags",
) {

    override fun generate(javaPath: Path) {
        val enumFile = DartFile.builder(packageName)
            .type(
                ClassSpec.enumClass(className)
                    .apply {
                        ItemHideFlag.entries.forEach { entry ->
                            val propertyName = entry.name.replace("HIDE_", "").lowercase()
                            enumProperty(
                                EnumPropertySpec.builder(propertyName)
                                    .parameter("%C", StringHelper.mapDisplayName(propertyName))
                                    .parameter("%C", entry.name)
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
                    .endWithNewLine(true)
                    .build()
            )
            .build()
        enumFile.write(javaPath)
    }

    override fun getName() = "DartItemFlagGenerator"
}
