package net.theevilreaper.stelaris.cli.generator.dart.util

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GeneratedFileTest : GenerationTestBase() {

    @Test
    fun `test plain source is written with the generated header`() {
        val file = writeGenerated(generationPath, "constants", "const int answer = 42;\n\n\n")

        assertEquals(generationPath.resolve("constants.dart"), file)
        assertEquals(generated("const int answer = 42;"), file.toFile().readText())
    }

    @Test
    fun `test file name with dart extension is not extended twice`() {
        val file = writeGenerated(generationPath, "constants.dart", "const int answer = 42;")

        assertEquals(generationPath.resolve("constants.dart"), file)
    }
}
