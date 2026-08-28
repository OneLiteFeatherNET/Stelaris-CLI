package net.theevilreaper.stelaris.cli.generator.dart.sound

import com.google.auto.service.AutoService
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
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

@AutoService(Generator::class)
@CodeGenerator(name = "SoundSourceGenerator")
class SoundSourceGenerator : BaseGenerator(
    className = "SoundSource",
    packageName = "sound"
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val soundSourceEntries = Sound.Source.entries
        val enumProperties = soundSourceEntries.map {
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
                    .modifier(DartModifier.CONST)
                    .enumProperties(*enumProperties.toTypedArray())
                    .properties(
                        PropertySpec.builder("displayName", String::class).modifier(DartModifier.FINAL).build(),
                        PropertySpec.builder("entry", String::class).modifier(DartModifier.FINAL).build()
                    )
                    .constructor {
                        ConstructorSpec.builder(className)
                            .modifier(DartModifier.CONST)
                            .parameter(ParameterSpec.positional("displayName",).build())
                            .parameter(ParameterSpec.positional("entry").build())
                            .build()
                    }
                    .build()
            )
            .build()
        soundSourceFile.write(folder)
    }
}