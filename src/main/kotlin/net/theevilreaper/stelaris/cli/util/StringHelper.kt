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
     * @param rawName the rawName which should be converted
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

    /**
     * Converts a given string to a lower camel case string.
     * @param input the input, which should be converted
     * @return the converted string
     */
    fun toLowerCamelCase(input: String): String {
        val parts = input
            .lowercase()
            .split('_')
            .filter { it.isNotEmpty() } // removes empty segments from "__"

        if (parts.isEmpty()) return ""

        return parts.first() +
                parts.drop(1).joinToString("") { it.replaceFirstChar(Char::uppercase) }
    }
}