package net.theevilreaper.stelaris.cli.arguments

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
    VERSION("version", "The version of the generated data, e.g. 26.2-rev1"),
    EXPERIMENTAL("experimental", "Enables experimental generators"),
    TYPE("type", "Where the data is exported to: local (default) or git"),
    PATH("path", "The folder of the Dart project, required for a local export")
    ;

    companion object {

        fun fromIdentifier(identifier: String): CommandArgument? {
            return entries.find { it.identifier == identifier }
        }
    }
}