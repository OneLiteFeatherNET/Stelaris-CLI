package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.util.VersionPart
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.PersonIdent
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.nio.file.Path

class GitProjectExporter(
    private val generationFolder: Path,
    private val versionString: String,
    private val versionPart: VersionPart,
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
        val gitRepo = cloneBaseRepo(
            generationFolder
        )

        gitRepo.add().addFilepattern(".").call()
        val commit = gitRepo.commit()
        commit.message = "Update version part: ${versionPart.part}"
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