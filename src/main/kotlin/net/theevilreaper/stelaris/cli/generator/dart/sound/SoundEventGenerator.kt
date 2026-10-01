package net.theevilreaper.stelaris.cli.generator.dart.sound

import com.google.auto.service.AutoService
import net.kyori.adventure.key.Key
import net.minestom.server.sound.SoundEvent
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartFileBuilder
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
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

@AutoService(Generator::class)
@CodeGenerator(name = "SoundEventGenerator")
class SoundEventGenerator : BaseGenerator(
    className = "SoundEvent",
    packageName = "sound",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val soundEntries = SoundEvent.values()
        val mappedEntries = SoundHelper.mapSoundEvents(soundEntries)

        val enumFiles = mutableListOf<DartFileBuilder>()
        mappedEntries.forEach { (key, value) ->
            val className = "${key.type.replaceFirstChar { it.uppercase() }}Sound"
            val fileName = "${key.type}_sound"

            val enumEntries = value
                .sortedBy { it.key().asString() }
                .distinctBy {
                    val parts = it.key().value().split(".")
                    if (parts.size >= 2) "${parts[1]}_${parts.last()}" else parts.last()
                }
                .map { buildEnumEntry(it.key()) }

            val enumClass = ClassSpec.enumClass(className)
                .apply {
                    enumEntries.forEach { enumProperty(it) }
                }
                .property(
                    PropertySpec.builder("displayName", String::class).modifier(DartModifier.FINAL).build()
                )
                .property(
                    PropertySpec.builder("key", String::class).modifier(DartModifier.FINAL).build()
                )
                .constructor(
                    ConstructorSpec.builder(className)
                        .modifier(DartModifier.CONST)
                        .parameter(ParameterSpec.positional("displayName").build())
                        .parameter(ParameterSpec.positional("key").build())
                        .build()
                )
                .build()
            val file = DartFile.builder(fileName)
                .type(enumClass)
            enumFiles.add(file)
        }


        // Write all enum files to the folder
        enumFiles.forEach { it.writeGenerated(folder) }
    }

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