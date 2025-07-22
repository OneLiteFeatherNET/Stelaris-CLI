package net.theevilreaper.stelaris.cli.generator.dart.sound

import net.kyori.adventure.key.Key
import net.minestom.server.sound.SoundEvent
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import org.slf4j.LoggerFactory

/**
 * The [SoundHelper] object provides utility functions for handling sound events in the context of the cli.
 *
 * @version 1.0.0
 * @author theEvilReaper
 * @since 0.1.0
 */
internal object SoundHelper {

    private val soundLogger = LoggerFactory.getLogger(SoundHelper::class.java)

    /**
     * Maps a collection of [SoundEvent]s to a map where the keys are [SoundType]s and the values are lists of [SoundEvent]s that belong to that type.
     * @param soundEvents the collection of [SoundEvent]s to map
     * @return a map where the keys are [SoundType]s and the values are lists of [SoundEvent]s
     */
    fun mapSoundEvents(soundEvents: Collection<SoundEvent>): Map<SoundType, List<SoundEvent>> {
        return soundEvents.mapNotNull { sound ->
            val soundKey: Key = sound.key()
            val soundDataString: List<String> = soundKey.value().split(".")
            val soundType: SoundType? = SoundType.fromType(soundDataString.first())
            if (soundType == null) {
                soundLogger.warn("Sound type not found for sound event: ${soundKey.asString()}")
                null
            } else {
                soundType to sound
            }
        }.groupBy({ it.first }, { it.second })
    }

    /**
     * Refactors the sound data string by removing dots and capitalizing the first letter of each part.
     * @param soundData the sound data string to refactor
     * @return a refactored string where each part is capitalized and dots are removed
     */
    fun refactorSoundData(soundData: String): String {
        val soundParts = soundData.replace("_", EMPTY_STRING).split(".")
        return soundParts.joinToString(separator = EMPTY_STRING) { it ->
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }
}
