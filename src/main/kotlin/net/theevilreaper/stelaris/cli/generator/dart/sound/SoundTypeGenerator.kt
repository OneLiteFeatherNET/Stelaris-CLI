package net.theevilreaper.stelaris.cli.generator.dart.sound

import com.google.auto.service.AutoService
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
@CodeGenerator(name = "SoundTypeGenerator")
class SoundTypeGenerator : BaseGenerator(
    className = "SoundType",
    packageName = "sound"
) {
    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)

        val entries = SoundType.entries
        val enumFile = ClassSpec.enumClass(className)
            .keyed(className)
            .apply {
                entries.forEach { soundType ->
                    enumProperty(EnumEntrySpec.builder(soundType.type)
                        .parameter(
                            EnumParameterSpec.positional(
                                "%C",
                                StringHelper.mapDisplayName(soundType.name)
                            )
                        )
                        .parameter(EnumParameterSpec.positional("%C", soundType.type))
                        .build()
                    )
                }
            }
            .properties(*DEFAULT_PROPERTIES)
            .constructor {
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(*DEFAULT_PARAMETERS)
                    .build()
            }
            .build()
        val file = DartFile.builder("sound_type")
            .keyedLookup(className)
            .type(enumFile)
        file.writeGenerated(folder, baseDir = outputPath.parent)
    }
}