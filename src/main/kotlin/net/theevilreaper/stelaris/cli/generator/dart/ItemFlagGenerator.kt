package net.theevilreaper.stelaris.cli.generator.dart

import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import java.nio.file.Path

class ItemFlagGenerator : BaseGenerator<Any>(
    className = "ItemFlags",
    packageName = "item_flags",
) {

    override fun generate(javaPath: Path) {
        /*val enumFile = DartFile.builder(packageName)
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
        enumFile.write(javaPath)*/
    }

    override fun getName() = "DartItemFlagGenerator"
}
