package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.stelaris.cli.generator.Generator
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.PersonIdent
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.nio.file.Files
import java.nio.file.Path

class GitProjectExporter(
    private val generationFolder: Path,
    private val versionString: String,
    private val generators: Set<Generator>
) : BaseExporter() {

    private val userName: String = System.getenv("stelaris.cli.username") ?: ""
    private val password: String = System.getenv("stelaris.cli.password") ?: ""
    private val cloneUrl: String = System.getenv("stelaris.cli.cloneUrl") ?: ""

    init {
        require(userName.isNotEmpty()) { "The username can't be empty" }
        require(password.isNotEmpty()) { "The password can't be empty" }
        require(cloneUrl.isNotEmpty()) { "The clone url can't be empty" }
    }

    override fun export() {
        cloneBaseRepo(generationFolder).use { gitRepo ->
            val libFolder = generationFolder.resolve("lib")

            if (!Files.exists(libFolder)) Files.createDirectories(libFolder)

            generators.forEach { generator -> generator.generate(libFolder) }

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

            gitPush.call()
        }
    }

    /**
     * Clones the base repository to the given path.
     * @param username the username for the repository
     * @param token the token for the repository
     * @param cloneUrl the clone URL for the repository
     * @param temp the temporary path to store the repository
     * @return the cloned repository
     */
    private fun cloneBaseRepo(temp: Path): Git {
        require(cloneUrl.trim().isNotEmpty()) { "Clone URL must not be empty" }
        val rawGit =
            Git.cloneRepository().setCredentialsProvider(
                UsernamePasswordCredentialsProvider(
                    userName,
                    password
                )
            ).setURI(cloneUrl).setDirectory(temp.toFile()).setCloneAllBranches(true)
        return rawGit.call()
    }
}
