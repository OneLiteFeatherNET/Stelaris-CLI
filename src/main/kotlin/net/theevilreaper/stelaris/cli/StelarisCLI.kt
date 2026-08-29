package net.theevilreaper.stelaris.cli

import com.google.inject.Guice
import net.minestom.server.MinecraftServer
import net.theevilreaper.stelaris.cli.arguments.CommandArgument
import net.theevilreaper.stelaris.cli.arguments.ParsedArgs
import net.theevilreaper.stelaris.cli.exporter.ExportStrategy
import net.theevilreaper.stelaris.cli.exporter.GitProjectExporter
import net.theevilreaper.stelaris.cli.exporter.LocalProjectExporter
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.GeneratorModule
import net.theevilreaper.stelaris.cli.generator.GeneratorRegistry
import net.theevilreaper.stelaris.cli.util.*
import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(HELP_MESSAGE)
        return
    }

    val parsedArgs = parseArguments(args)

    if (parsedArgs.showHelp) {
        println(HELP_MESSAGE)
        return
    }

    if (parsedArgs.version.isBlank()) {
        println("Please specify a valid version")
        return
    }

    val registry = Guice.createInjector(GeneratorModule())
        .getInstance(GeneratorRegistry::class.java)
    val generators: Set<Generator> = registry.createGenerators {
        parsedArgs.experimental || !it.experimental
    }

    // Use the user-specified path if provided; otherwise, use the default for Git
    val isTempDir = parsedArgs.localBuild && parsedArgs.path == null || !parsedArgs.localBuild
    val workingDir = when {
        parsedArgs.localBuild -> parsedArgs.path ?: Files.createTempDirectory(TEMP_DIR_NAME)
        else -> Files.createTempDirectory(TEMP_DIR_NAME)
    }

    try {
        MinecraftServer.init()
        val parsedVersion = parsedArgs.version

        val projectExporter = when (parsedArgs.localBuild) {
            true -> LocalProjectExporter(workingDir, generators)
            false -> GitProjectExporter(workingDir, parsedVersion, generators)
        }

        projectExporter.export()
    } finally {
        if (isTempDir) {
            workingDir.toFile().deleteRecursively()
        }
    }
}

private fun parseArguments(args: Array<String>): ParsedArgs {
    var showHelp = false
    var experimental = false
    var localBuild = true
    var path: Path? = null
    var version: String? = null

    args.forEachIndexed { index, arg ->
        if (arg.startsWith(ARGUMENT_IDENTIFIER)) {
            val argument: String = arg.substring(1, arg.length)
            val commandArg: CommandArgument? = CommandArgument.fromIdentifier(argument)

            if (commandArg == null) {
                println("The argument $argument is not supported")
                return@forEachIndexed
            }

            when (commandArg) {
                CommandArgument.HELP -> showHelp = true
                CommandArgument.VERSION -> {
                    if (index + 1 < args.size) {
                        version = args[index + 1]
                    } else {
                        println("Missing value for argument $argument")
                    }
                }
                CommandArgument.EXPERIMENTAL -> experimental = true
                CommandArgument.TYPE -> {
                    if (index + 1 < args.size) {
                        val type = args[index + 1]
                        val exportStrategy = ExportStrategy.fromIdentifier(type)
                        if (exportStrategy == ExportStrategy.GIT) {
                            localBuild = false
                        }
                    } else {
                        println("Missing value for argument $argument")
                    }
                }

                CommandArgument.PATH -> {
                    if (index + 1 < args.size) {
                        val pathString = args[index + 1]
                        path = Path.of(pathString)
                    } else {
                        println("Missing value for argument $argument")
                    }
                }
            }
        }
    }

    return ParsedArgs(showHelp, version ?: "", experimental, localBuild, path)
}
