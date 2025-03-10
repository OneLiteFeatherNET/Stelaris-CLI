package net.theevilreaper.stelaris.cli.generator.dart

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

/**
 * This generator type contains the logic to generate the [BossBar] enum structure to enums for dart.
 * A generation process triggers all enum generation, and it is not possible to skip one type
 * @author theEvilReaper
 * @since 1.0.0
 */
class BossBarGenerator : BaseGenerator(
    className = "BossBarColor",
    packageName = "boss_bar_color",
) {
    private val overlayClass = "BossOverlay"
    private val overlayFile = "boss_overlay"
    private val flagClass = "BossFlag"
    private val flagFile = "boss_flag"
    private val stringIdentifier = "%C"

    /**
     * Triggers the generation for all relevant data for the [BossBar].
     * @param javaPath the path to write the code into
     */
    override fun generate(javaPath: Path) {
        generateBossColors().write(javaPath)
        generateStyle().write(javaPath)
        generateFlags().write(javaPath)
    }

    /**
     * Generates the [DartFile] which contains all values from the [BossBar.Color] class.
     */
    private fun generateBossColors(): DartFile {
        return DartFile.builder(packageName)
            .type(
                ClassSpec.enumClass(className)
                    .apply {
                        BossBar.Color.entries.forEach { color ->
                            enumProperty(
                                EnumEntrySpec.builder(color.name.lowercase())
                                    .parameter(
                                        EnumParameterSpec.positional(
                                            stringIdentifier,
                                            StringHelper.mapDisplayName(color.name)
                                        )
                                    )
                                    .parameter(EnumParameterSpec.positional(stringIdentifier, color.name.uppercase()))
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
    }

    /**
     * Generates the [DartFile] which contains all values from the [BossBar.Overlay] class.
     */
    private fun generateStyle(): DartFile {
        return DartFile.builder(overlayFile)
            .type(
                ClassSpec.enumClass(overlayClass)
                    .apply {
                        BossBar.Overlay.entries.forEach { overlay ->
                            enumProperty(
                                EnumEntrySpec.builder(overlay.name.lowercase())
                                    .parameter(
                                        EnumParameterSpec.positional(
                                            stringIdentifier,
                                            StringHelper.mapDisplayName(overlay.name)
                                        )
                                    )
                                    .parameter(EnumParameterSpec.positional(stringIdentifier, overlay.name.uppercase()))
                                    .build()
                            )
                        }
                    }
                    .properties(*DEFAULT_PROPERTIES)
                    .constructor(
                        ConstructorSpec.builder(overlayClass)
                            .modifier(DartModifier.CONST)
                            .parameters(*DEFAULT_PARAMETERS)
                            .build()
                    )
            )
            .build()
    }

    /**
     * Generates the [DartFile] which contains all values from the [BossBar.Flag] class.
     */
    private fun generateFlags(): DartFile {
        return DartFile.builder(flagFile)
            .type(
                ClassSpec.enumClass(flagClass)
                    .apply {
                        BossBar.Flag.entries.forEach { overlay ->
                            enumProperty(
                                EnumEntrySpec.builder(overlay.name.lowercase())
                                    .parameter(
                                        EnumParameterSpec.positional(
                                            stringIdentifier,
                                            StringHelper.mapDisplayName(overlay.name)
                                        )
                                    )
                                    .parameter(EnumParameterSpec.positional(stringIdentifier, overlay.name.uppercase()))
                                    .build()
                            )
                        }
                    }
                    .properties(*DEFAULT_PROPERTIES)
                    .constructor(
                        ConstructorSpec.builder(flagClass)
                            .modifier(DartModifier.CONST)
                            .parameters(*DEFAULT_PARAMETERS)
                            .build()
                    )
            )
            .build()
    }

    /**
     * Returns the name from the generator as [String].
     * @return the given name
     */
    override fun getName(): String = "BossBarGenerator"

    override fun isExperimental() = false
}
