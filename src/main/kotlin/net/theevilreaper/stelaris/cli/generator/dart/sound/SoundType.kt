package net.theevilreaper.stelaris.cli.generator.dart.sound

/**
 * The [SoundType] enum is a group representing different categories of sounds in the game.
 * Each type corresponds to a specific category of sound events, such as blocks, entities, music, items, and ambient sounds.
 *
 * **This enum doesn't reflect the actual sound categories from the game with 100% accuracy.**
 *
 * @property type The string representation of the sound type.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
enum class SoundType(val type: String) {

    BLOCK("block"),
    ENTITY("entity"),
    MUSIC("music"),
    ITEM("item"),
    AMBIENT("ambient")
    ;

    companion object {

        /**
         * Finds a [SoundType] by its type string.
         * @param type the type string to search for.
         * @return the [SoundType] corresponding to the type string, or null if not found.
         */
        @JvmStatic
        fun fromType(type: String): SoundType? {
            return entries.find { it.type == type }
        }
    }
}