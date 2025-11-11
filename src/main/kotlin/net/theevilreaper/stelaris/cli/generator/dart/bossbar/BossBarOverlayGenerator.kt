package net.theevilreaper.stelaris.cli.generator.dart.bossbar

import net.kyori.adventure.bossbar.BossBar
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

class BossBarOverlayGenerator: BaseGenerator(
    className = "BossBarOverlay",
    packageName = "bossbar"
) {
    override fun generate(javaPath: Path) {
        val folder = checkPackageFolder(javaPath, packageName)
        val file = DartFile.builder("boss_bar_overlay")
            .type(
                ClassSpec.enumClass(className)
                    .apply {
                        BossBar.Overlay.entries.forEach { overlay ->
                            enumProperty(
                                EnumEntrySpec.builder(overlay.name.lowercase())
                                    .parameter(
                                        EnumParameterSpec.positional(
                                            "%C",
                                            StringHelper.mapDisplayName(overlay.name)
                                        )
                                    )
                                    .parameter(EnumParameterSpec.positional("%C", overlay.name.uppercase()))
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
            )
            .build()
        file.write(folder)
    }

    override fun getName(): String = "BossbarOverlayGenerator"
}