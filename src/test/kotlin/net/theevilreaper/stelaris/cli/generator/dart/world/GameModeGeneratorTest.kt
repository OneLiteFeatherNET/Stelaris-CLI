package net.theevilreaper.stelaris.cli.generator.dart.world

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GameModeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test game mode generation`() {
        val generator = GameModeGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("world").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("game_mode.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum GameMode"), "Generated file should declare GameMode enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final int id;"))
        assertTrue(content.contains("survival('Survival', 0)"))
        assertTrue(content.contains("creative('Creative', 1)"))
        assertTrue(content.contains("adventure('Adventure', 2)"))
        assertTrue(content.contains("spectator('Spectator', 3)"))
    }
}
