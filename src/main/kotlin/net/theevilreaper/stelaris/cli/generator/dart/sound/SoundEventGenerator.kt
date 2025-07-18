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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path

class SoundEventGenerator : BaseGenerator(
    className = "SoundEvent",
    packageName = "sound",
) {

    private val soundLogger: Logger = LoggerFactory.getLogger(SoundEventGenerator::class.java)

    override fun generate(javaPath: Path) {
        val folder = javaPath.resolve("sound")
        if (!Files.exists(folder)) {
            Files.createDirectory(folder)
        }
        val soundEntries = SoundEvent.values()
        val mappedEntries: Map<SoundType, List<SoundEvent>> = soundEntries.mapNotNull { sound ->
            val soundKey: Key = sound.key()
            val soundDataString: List<String> = soundKey.value().split(".")
            val soundType: SoundType? = SoundType.fromType(soundDataString.first())

            if (soundType == null) {
                soundLogger.warn("Sound event ${soundKey.value()} does not have a valid type, skipping.")
                null
            } else {
                soundType to sound
            }
        }.groupBy({ it.first }, { it.second })

        mappedEntries.forEach { (key, value) ->
            
        }


        val enumClass = ClassSpec.enumClass(className)
            .apply {
                val entries = SoundEvent.values()
                entries.forEach {
                    val soundKey: Key = it.key()
                    println("Adding sound event: ${soundKey.value()}")
                    val splittedSoundData = soundKey.value().split(".")
                    enumProperty(
                        EnumEntrySpec.builder(it.name())
                            .parameter(EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(it.name())))
                            .build()
                    )
                }
            }
            .property(PropertySpec.builder("name", String::class).modifier(DartModifier.FINAL).build())
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .build()
            )
            .build()
        val file = DartFile.builder(packageName)
            .doc("Generated class for the sound sources. Don't edit this file manually")
            .type(enumClass)
            .build()
        file.write(folder)
    }

    override fun getName(): String = "SoundTypeGenerator"

    override fun isExperimental() = false
}