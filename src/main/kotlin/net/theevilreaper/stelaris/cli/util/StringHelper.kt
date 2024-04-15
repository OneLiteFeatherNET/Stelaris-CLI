package net.theevilreaper.stelaris.cli.util

/**
 * Helper class for string operations.
 * @since 1.0
 * @version 1.0
 * @author theEvilReaper
 */
object StringHelper {

    /**
     * Converts a given string to a name which can be shown in an ui field.
     * @param rawName the name which should be converted
     * @return the converted name
     */
    fun mapDisplayName(rawName: String): String {
        if (!rawName.contains("_")) {
            return rawName.lowercase().replaceFirstChar { it.uppercase() }
        }

        return rawName.split("_").joinToString(separator = SPACE_STRING) {
            it.lowercase().replaceFirstChar {
                char -> char.uppercase()
            }
        }
    }
}