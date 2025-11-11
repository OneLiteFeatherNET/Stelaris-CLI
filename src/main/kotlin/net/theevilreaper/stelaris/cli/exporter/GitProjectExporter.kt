package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.util.VersionPart
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.PersonIdent
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class GitProjectExporter(
    private val generationFolder: Path,
    private val versionString: String,
    private val generators: Set<Generator>
) : BaseExporter() {

    private val userName: String
    private val password: String
    private val cloneUrl: String

    init {
        require((versionString.isNotEmpty())) { "The version string can't be empty" }

        userName = System.getenv("stelaris.cli.username")
        password = System.getenv("stelaris.cli.password")
        cloneUrl = System.getenv("stelaris.cli.cloneUrl")

        require((userName.isNotEmpty())) { "The username can't be empty" }
        require((password.isNotEmpty())) { "The password can't be empty" }
        require((cloneUrl.isNotEmpty())) { "The clone url can't be empty" }
    }

    override fun export() {
     //   Files.copy(templateStream, generationFolder, StandardCopyOption.REPLACE_EXISTING)

        val gitRepo = Git.init().setDirectory(generationFolder.toFile()).call()

        val libPath: Path = generationFolder.resolve("lib")
        if (!Files.exists(libPath)) Files.createDirectory(libPath)
        modifyPubSpecFile(generationFolder, versionString)

        generators.forEach { generator -> generator.generate(generationFolder) }

        gitRepo.add().addFilepattern(".").call()
        val commit = gitRepo.commit()
        commit.message = "Update version to $versionString"
        commit.author = PersonIdent("Stelaris CLI", "gitlab+generator@onelitefeather.net")
        commit.setAll(true)
        commit.call()

        val gitPush = gitRepo.push()

        gitPush.setCredentialsProvider(
            UsernamePasswordCredentialsProvider(
                userName,
                password
            )
        )

        gitPush.isForce = true
        gitPush.call()
    }
}