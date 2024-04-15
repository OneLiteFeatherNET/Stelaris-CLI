package net.theevilreaper.stelaris.cli.util

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.nio.file.Path

fun cloneBaseRepo(username: String, token: String, cloneUrl: String, temp: Path): Git {
    require(username.trim().isNotEmpty()) { "Username must not be empty" }
    require(token.trim().isNotEmpty()) { "Token must not be empty" }
    require(cloneUrl.trim().isNotEmpty()) { "Clone URL must not be empty" }
    val rawGit =
        Git.cloneRepository().setCredentialsProvider(
            UsernamePasswordCredentialsProvider(
                username,
                token
            )
        ).setURI(cloneUrl).setDirectory(temp.toFile()).setCloneAllBranches(true)
    return rawGit.call()
}