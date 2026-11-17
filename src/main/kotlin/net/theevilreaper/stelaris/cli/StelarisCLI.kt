package net.theevilreaper.stelaris.cli

import com.google.inject.Guice
import net.minestom.server.MinecraftServer
import net.theevilreaper.stelaris.cli.arguments.ArgumentParser
import net.theevilreaper.stelaris.cli.arguments.InvalidArgumentsException
import net.theevilreaper.stelaris.cli.exporter.GitProjectExporter
import net.theevilreaper.stelaris.cli.exporter.LocalProjectExporter
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.GeneratorModule
import net.theevilreaper.stelaris.cli.generator.GeneratorRegistry
import net.theevilreaper.stelaris.cli.util.*
import java.nio.file.Files
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(HELP_MESSAGE)
        return
    }

    val parsedArgs = try {
        ArgumentParser.parse(args)
    } catch (exception: InvalidArgumentsException) {
        System.err.println(exception.message)
        System.err.println(HELP_MESSAGE)
        exitProcess(1)
    }

    if (parsedArgs.showHelp) {
        println(HELP_MESSAGE)
        return
    }

    val registry = Guice.createInjector(GeneratorModule())
        .getInstance(GeneratorRegistry::class.java)
    val generators: Set<Generator> = registry.createGenerators {
        parsedArgs.experimental || !it.experimental
    }

    // A local export always has a path, a git export clones into a temporary folder
    val workingDir = parsedArgs.path ?: Files.createTempDirectory(TEMP_DIR_NAME)

    try {
        MinecraftServer.init()
        val projectExporter = when (parsedArgs.localBuild) {
            true -> LocalProjectExporter(workingDir, generators)
            false -> GitProjectExporter(workingDir, parsedArgs.version, generators)
        }

        projectExporter.export()
    } finally {
        if (parsedArgs.path == null) {
            workingDir.toFile().deleteRecursively()
        }
    }
}
