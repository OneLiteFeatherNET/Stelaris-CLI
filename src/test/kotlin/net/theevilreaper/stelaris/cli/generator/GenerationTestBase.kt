package net.theevilreaper.stelaris.cli.generator

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

abstract class GenerationTestBase {

    @TempDir
    protected lateinit var generationPath: Path

    @BeforeEach
    fun setup() {
        val files = Files.list(generationPath).toList()
        check(files.isEmpty()) { "Expected generation folder to be empty, but found: $files" }
    }
}