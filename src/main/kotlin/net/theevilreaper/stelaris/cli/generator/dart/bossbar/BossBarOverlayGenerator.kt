package net.theevilreaper.stelaris.cli.generator.dart.bossbar

import com.google.auto.service.AutoService
import net.kyori.adventure.bossbar.BossBar
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
@CodeGenerator(name = "BossBarOverlayGenerator")
class BossBarOverlayGenerator : BaseGenerator(
    className = "BossBarOverlay",
    packageName = "bossbar"
) {
    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val file = DartFile.builder("boss_bar_overlay")
            .keyedLookup(className)
            .type(
                ClassSpec.enumClass(className)
                    .keyed(className)
                    .apply {
                        BossBar.Overlay.entries.forEach { overlay ->
                            enumProperty(
                                EnumEntrySpec.builder(StringHelper.toLowerCamelCase(overlay.name))
                                    .parameter(
                                        EnumParameterSpec.positional(
                                            "%C",
                                            StringHelper.mapDisplayName(overlay.name)
                                        )
                                    )
                                    .parameter(EnumParameterSpec.positional("%C", overlay.name.lowercase()))
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
            )
        file.writeGenerated(folder, baseDir = outputPath.parent)
    }
}