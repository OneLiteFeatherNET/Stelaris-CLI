package net.theevilreaper.stelaris.cli.generator

import net.theevilreaper.stelaris.cli.generator.dart.util.GENERATED_FILE_HEADER
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

    /**
     * Returns the full content of a generated file with the given [body] after the generated header.
     * @param body the content after the header
     * @return the expected content of the generated file
     */
    protected fun generated(body: String): String = "$GENERATED_FILE_HEADER\n\n$body\n"
}