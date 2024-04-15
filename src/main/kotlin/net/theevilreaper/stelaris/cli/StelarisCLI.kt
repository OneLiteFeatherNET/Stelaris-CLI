package net.theevilreaper.stelaris.cli

import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.GeneratorRegistry
import net.theevilreaper.stelaris.cli.util.*
import org.eclipse.jgit.lib.PersonIdent
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
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
    var generators: Set<Generator> = generatorRegistry.generators

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
                CommandArgument.UPDATE -> {
                    val versionPartString = args[index + 1]
                    if (versionPartString.startsWith(ARGUMENT_IDENTIFIER)) {
                        println("A version part can't start with an $ARGUMENT_IDENTIFIER")
                        return@forEachIndexed
                    }

                    versionPart = VersionPart.parse(versionPartString)
                }
                CommandArgument.EXPERIMENTAL -> generators = generatorRegistry.generators
            }
        } else {
            println("Unknown argument: $arg")
        }
    }

    if (showHelp) {
        println(HELP_MESSAGE)
    }

    if (generators.isEmpty()) {
        println("The cli needs generators to run")
        return
    }

    val tempFile: Path = Files.createTempDirectory(TEMP_DIR_NAME)

    generators.forEach { it.generate(tempFile) }

    val gitRepo = cloneBaseRepo(
        System.getenv("stelaris.cli.username"),
        System.getenv("stelaris.cli.password"),
        System.getenv("stelaris.cli.cloneUrl"),
        tempFile
    )

    gitRepo.add().addFilepattern(".").call()
    val commit = gitRepo.commit()
    commit.message = "Update version part: ${versionPart!!.part}"
    commit.setAuthor(PersonIdent("Stelaris CLI", "gitlab+generator@onelitefeather.net"))
    commit.setAll(true)
    commit.call()

    val gitPush = gitRepo.push()

    gitPush.setCredentialsProvider(
        UsernamePasswordCredentialsProvider(
            System.getenv("stelaris.cli.username"),
            System.getenv("stelaris.cli.password")
        )
    )

    gitPush.setForce(true)
    gitPush.call()
}
