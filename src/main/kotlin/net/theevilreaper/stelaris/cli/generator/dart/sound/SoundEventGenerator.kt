package net.theevilreaper.stelaris.cli.generator.dart.sound

import net.kyori.adventure.key.Key
import net.minestom.server.sound.SoundEvent
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

class SoundEventGenerator : BaseGenerator(
    className = "SoundEvent",
    packageName = "sound",
) {

    override fun generate(javaPath: Path) {
        val folder = checkPackageFolder(javaPath, packageName)
        val soundEntries = SoundEvent.values()
        val mappedEntries = SoundHelper.mapSoundEvents(soundEntries)

        val enumFiles = mutableListOf<DartFile>()
        mappedEntries.forEach { (key, value) ->
            val className = "${key.type.replaceFirstChar { it.uppercase() }}Sound"
            val fileName = "${key.type}_sound"

            val enumClass = ClassSpec.enumClass(className)
                .apply {
                    value.forEach { it ->
                        val soundKey: Key = it.key()
                        val soundData = soundKey.value().split(".")
                        val soundType = soundData[1]
                        val variant = soundData.last()
                        val soundTypeTitle = SoundHelper.refactorSoundData(soundType)
                        val soundVariant = SoundHelper.refactorSoundData(variant)
                        val displayName = StringHelper.mapDisplayName("$soundTypeTitle $soundVariant")
                        enumProperty(
                            EnumEntrySpec.builder("${soundType}_$variant")
                                .parameter(EnumParameterSpec.positional("%C", displayName))
                                .parameter(EnumParameterSpec.positional("%C", it.key().asString()))
                                .build()
                        )
                    }
                }
                .property(
                    PropertySpec.builder("name", String::class).modifier(DartModifier.FINAL).build()
                )
                .property(
                    PropertySpec.builder("key", String::class).modifier(DartModifier.FINAL).build()
                )
                .constructor(
                    ConstructorSpec.builder(className)
                        .modifier(DartModifier.CONST)
                        .build()
                )
                .build()
            val file = DartFile.builder(fileName)
                .doc("Generated class for the sound sources. Don't edit this file manually")
                .type(enumClass)
                .build()
            enumFiles.add(file)
        }


        // Write all enum files to the folder
        enumFiles.forEach { it.write(folder) }
    }

    override fun getName(): String = "SoundTypeGenerator"
}