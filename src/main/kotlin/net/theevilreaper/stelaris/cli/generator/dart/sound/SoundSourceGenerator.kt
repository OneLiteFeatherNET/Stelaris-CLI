package net.theevilreaper.stelaris.cli.generator.dart.sound

import net.kyori.adventure.sound.Sound
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Files
import java.nio.file.Path

class SoundSourceGenerator : BaseGenerator(
    className = "SoundSource",
    packageName = "sound"
) {

    override fun generate(javaPath: Path) {
        val folder = javaPath.resolve("sound")
        if (!Files.exists(folder)) {
            Files.createDirectory(folder)
        }

        val soundSourceEntries = Sound.Source.entries
        val enumProperties = soundSourceEntries.map { it ->
            val source = it.name
            EnumEntrySpec.builder(source)
                .parameter(
                    EnumParameterSpec.positional(
                        "%C",
                        StringHelper.mapDisplayName(source)
                    )
                )
                .parameter(EnumParameterSpec.positional("%C", source))
                .build()
        }
        val soundSourceFile = DartFile.builder("sound_source")
            .type(
                ClassSpec.enumClass(className)
                    .enumProperties(*enumProperties.toTypedArray())
                    .properties(
                        PropertySpec.builder("displayName", String::class).modifier(DartModifier.FINAL).build(),
                        PropertySpec.builder("entry", String::class).modifier(DartModifier.FINAL).build()
                    )
                    .constructor {
                        ConstructorSpec.builder(className)
                            .parameter(ParameterSpec.positional("displayName", String::class.java).build())
                            .parameter(ParameterSpec.positional("entry", String::class.java).build())
                            .build()
                    }
                    .build()
            )
            .build()
        soundSourceFile.write(folder)
    }

    override fun getName(): String = "SoundSourceGenerator"
}