package net.theevilreaper.stelaris.cli.generator.dart.sound

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
import java.nio.file.Path

class SoundTypeGenerator: BaseGenerator(
    className = "SoundType",
    packageName = "sound"
) {
    override fun generate(javaPath: Path) {
        val folder = checkPackageFolder(javaPath, packageName)

        val entries = SoundType.entries
        val enumFile = ClassSpec.enumClass(className)
            .apply {
                entries.forEach { soundType ->
                    enumProperty(EnumEntrySpec.builder(soundType.type)
                        .parameter(
                            EnumParameterSpec.positional(
                                "%C",
                                StringHelper.mapDisplayName(soundType.name)
                            )
                        )
                        .parameter(EnumParameterSpec.positional("%C", soundType.name))
                        .build()
                    )
                }
            }
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
        val file = DartFile.builder("sound_type")
            .doc("Generated class for the sound sources. Don't edit this file manually")
            .type(enumFile)
            .build()
        file.write(folder)
    }

    override fun getName(): String = "SoundTypeGenerator"
}