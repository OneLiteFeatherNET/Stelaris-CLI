package net.theevilreaper.stelaris.cli.generator.dart

import net.kyori.adventure.sound.Sound
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

class SoundTypeGenerator : BaseGenerator<Any>(
    className = "SoundSource",
    packageName = "sound",
) {
    override fun generate(javaPath: Path) {
        val enumClass = ClassSpec.enumClass(className)
            .apply {
                val entries = Sound.Source.entries
                entries.forEach {
                    enumProperty(
                        EnumPropertySpec
                            .builder(it.name.lowercase())
                            .parameter("%C", StringHelper.mapDisplayName(it.name))
                            .build()
                    )
                }
            }
            .property(PropertySpec.builder("name", String::class).modifier(DartModifier.FINAL).build())
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameter(ParameterSpec.builder("name").build())
                    .build()
            )
            .build()
        val file = DartFile.builder(packageName)
            .doc("Generated class for the sound sources. Don't edit this file manually")
            .type(enumClass)
            .build()
        file.write(javaPath)
    }

    override fun getName(): String = "SoundTypeGenerator"
}
