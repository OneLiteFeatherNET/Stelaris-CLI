package net.theevilreaper.stelaris.cli.arguments

import net.theevilreaper.stelaris.cli.exporter.ExportStrategy
import net.theevilreaper.stelaris.cli.util.ARGUMENT_IDENTIFIER
import java.nio.file.Path

/**
 * Thrown when the arguments of the CLI can't be used, e.g. an unknown argument or a missing value.
 * @param message describes what is wrong with the arguments
 * @since 1.0.0
 */
class InvalidArgumentsException(message: String) : IllegalArgumentException(message)

/**
 * Parses the command line arguments of the CLI.
 *
 * Every mistake fails the parsing instead of being skipped, so a wrong call never ends as a run which
 * generates nothing. A local export needs a `-path`, because without one the output would be written to a
 * temporary folder which is deleted right after the generation.
 * @since 1.0.0
 */
object ArgumentParser {

    /**
     * Parses the given [args].
     * @param args the arguments as passed to the CLI
     * @return the parsed arguments
     * @throws InvalidArgumentsException if the arguments can't be used
     */
    fun parse(args: Array<String>): ParsedArgs {
        var showHelp = false
        var experimental = false
        var version: String? = null
        var strategy = ExportStrategy.LOCAL
        var path: Path? = null

        var index = 0
        while (index < args.size) {
            val arg = args[index]
            if (!arg.startsWith(ARGUMENT_IDENTIFIER)) {
                throw InvalidArgumentsException("Unexpected value '$arg'")
            }
            val argument = CommandArgument.fromIdentifier(arg.substring(1))
                ?: throw InvalidArgumentsException("The argument $arg is not supported")
            when (argument) {
                CommandArgument.HELP -> showHelp = true
                CommandArgument.EXPERIMENTAL -> experimental = true
                CommandArgument.VERSION -> version = valueOf(args, index++)
                CommandArgument.TYPE -> {
                    val type = valueOf(args, index++)
                    strategy = ExportStrategy.fromIdentifier(type)
                        ?: throw InvalidArgumentsException(
                            "Unknown type '$type', use one of: ${ExportStrategy.entries.joinToString { it.identifier }}"
                        )
                }
                CommandArgument.PATH -> path = Path.of(valueOf(args, index++))
            }
            index++
        }

        if (!showHelp) {
            if (version.isNullOrBlank()) throw InvalidArgumentsException("Please specify a version with -version")
            when (strategy) {
                ExportStrategy.LOCAL -> if (path == null) {
                    throw InvalidArgumentsException("A local export needs the folder of the project with -path")
                }

                ExportStrategy.GIT -> if (path != null) {
                    throw InvalidArgumentsException("A git export clones the repository itself and takes no -path")
                }
            }
        }

        return ParsedArgs(showHelp, version.orEmpty(), experimental, strategy == ExportStrategy.LOCAL, path)
    }

    /**
     * Returns the value of the argument at [index], which is the next element of the [args].
     */
    private fun valueOf(args: Array<String>, index: Int): String {
        val value = args.getOrNull(index + 1)
        if (value == null || value.startsWith(ARGUMENT_IDENTIFIER)) {
            throw InvalidArgumentsException("Missing value for argument ${args[index]}")
        }
        return value
    }
}
