package net.theevilreaper.stelaris.cli.util

import net.theevilreaper.stelaris.cli.util.exception.UnknownVersionPartException
import kotlin.jvm.Throws

/**
 * Represents the different parts of a version string.
 * @param part the part of the version string
 * @version 1.0
 * @since 1.0
 * @author theEvilReaper
 */
enum class VersionPart(val part: String) {
    MAJOR("major"),
    MINOR("minor"),
    PATCH("patch"),
    REVISION("rev");

    companion object {

        /**
         * Parses a given string to a version part.
         * @param part the part which should be parsed
         * @return the parsed version part
         * @throws UnknownVersionPartException if the part is unknown
         * @since 1.0
         * @version 1.0
         */
        @JvmStatic
        @Throws(UnknownVersionPartException::class)
        fun parse(part: String): VersionPart {
            if (part.trim().isNotEmpty()) {
                return entries.find { it.part == part }
                    ?: throw UnknownVersionPartException("Unknown version part: $part")
            } else {
                throw UnknownVersionPartException("The version part is empty")
            }
        }

        fun getTextualRepresentation(): String {
            return entries.joinToString(prefix = "<", separator = ", ", postfix = ">") { it.part }
        }
    }
}