package net.theevilreaper.stelaris.cli.util

/**
 * Represents the different command arguments which can be used.
 * @since 1.0
 * @version 1.0
 * @author theEvilReaper
 * @param identifier the identifier of the command argument
 * @param helpMessage the help message which should be shown
 */
enum class CommandArgument(val identifier: String, val helpMessage: String) {

    HELP("help", "Shows this help message"),
    UPDATE("update", "Updates a specific part of the version string"),
    EXPERIMENTAL("experimental", "Enables experimental generators"),
    GIT("git", "Enables the git support");

    companion object {

        fun fromIdentifier(identifier: String): CommandArgument? {
            return entries.find { it.identifier == identifier }
        }
    }
}
