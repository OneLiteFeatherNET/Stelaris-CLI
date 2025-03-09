package net.theevilreaper.stelaris.cli

import net.minestom.server.MinecraftServer
import net.theevilreaper.stelaris.cli.arguments.CommandArgument
import net.theevilreaper.stelaris.cli.exporter.ExportStrategy
import net.theevilreaper.stelaris.cli.exporter.GitProjectExporter
import net.theevilreaper.stelaris.cli.exporter.LocalProjectExporter
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.GeneratorRegistry
import net.theevilreaper.stelaris.cli.util.*
import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(HELP_MESSAGE)
        return
    }

    val generatorRegistry = GeneratorRegistry()
    var showHelp = false
    var versionPart: VersionPart? = null
    var experimental = false
    var localBuild = true

    args.forEachIndexed { index, arg ->
        if (arg.startsWith(ARGUMENT_IDENTIFIER)) {
            val argument: String = arg.substring(1, arg.length)
            println("Argument: $argument")
            val commandArg: CommandArgument? = CommandArgument.fromIdentifier(argument)
            println("Command argument: $commandArg")

            if (commandArg == null) {
                println("The argument $argument is not supported")
                return@forEachIndexed
            }

            when (commandArg) {
                CommandArgument.HELP -> showHelp = true
                CommandArgument.UPDATE -> {
                    val versionPartString = args[index + 1]
                    if (versionPartString.startsWith(ARGUMENT_IDENTIFIER)) {
                        println("A version part can't start with an $ARGUMENT_IDENTIFIER")
                        return@forEachIndexed
                    }

                    versionPart = VersionPart.parse(versionPartString)
                }

                CommandArgument.EXPERIMENTAL -> experimental = true
                CommandArgument.TYPE -> {
                    val type = args[index + 1]
                    val exportStrategy = ExportStrategy.fromIdentifier(type)
                    if (exportStrategy == ExportStrategy.GIT) {
                        localBuild = false
                    }
                }
            }
        }
    }

    if (showHelp) {
        println(HELP_MESSAGE)
    }

    val generators: Set<Generator> = when(experimental) {
        false -> generatorRegistry.getGenerators { !it.isExperimental() }
        true -> generatorRegistry.getGenerators()
    }

    if (generators.isEmpty()) {
        println("The cli needs generators to run")
        return
    }

    val tempFile: Path = Files.createTempDirectory(TEMP_DIR_NAME)

    MinecraftServer.init();

    val projectExporter = when(localBuild) {
        true -> LocalProjectExporter(tempFile, "", generators)
        false -> GitProjectExporter(tempFile, "", versionPart = versionPart!!, generators)
    }

    projectExporter.export()
}
