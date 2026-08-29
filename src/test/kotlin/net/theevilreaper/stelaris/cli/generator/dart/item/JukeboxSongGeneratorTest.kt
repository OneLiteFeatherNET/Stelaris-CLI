package net.theevilreaper.stelaris.cli.generator.dart.item

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JukeboxSongGeneratorTest : GenerationTestBase() {

    @Test
    fun `test jukebox song generation`() {
        val generator = JukeboxSongGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("item").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("jukebox_song.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum JukeboxSong"), "Generated file should declare JukeboxSong enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final double lengthInSeconds;"))
        assertTrue(content.contains("final int comparatorOutput;"))
        assertTrue(content.contains("pigstep('Pigstep', 'minecraft:pigstep', 149.0, 13)"))
    }
}
