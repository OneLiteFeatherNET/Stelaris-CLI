package net.theevilreaper.stelaris.cli.generator.dart.sound

import net.kyori.adventure.key.Key
import net.minestom.server.sound.SoundEvent
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

            val enumEntries = value
                .distinctBy { it.key().value().split(".").let { parts -> "${parts[1]}_${parts.last()}" } }
                .map { buildEnumEntry(it.key()) }

            val enumClass = ClassSpec.enumClass(className)
                .apply {
                    enumEntries.forEach { enumProperty(it) }
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
                        .parameter(ParameterSpec.positional("name").build())
                        .parameter(ParameterSpec.positional("key").build())
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

    override fun getName(): String = "SoundEventGenerator"

    private fun buildEnumEntry(soundKey: Key): EnumEntrySpec {
        val parts = soundKey.value().split(".")
        require(parts.size >= 2) { "Invalid sound key: ${soundKey.value()}" }

        val soundType = parts[1]
        val variant = parts.last()

        val enumName = StringHelper.toLowerCamelCase("${soundType}_${variant}")
        val displayName = StringHelper.mapDisplayName("$soundType $variant")

        return EnumEntrySpec.builder(enumName)
            .parameter(EnumParameterSpec.positional("%C", displayName))
            .parameter(EnumParameterSpec.positional("%C", soundKey.asString()))
            .build()
    }
}