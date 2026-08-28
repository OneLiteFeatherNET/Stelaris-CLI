package net.theevilreaper.stelaris.cli.generator.dart.world

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BiomeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test biome generation`() {
        val generator = BiomeGenerator()
        assertEquals("BiomeGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("world").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("biome.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum Biome"), "Generated file should declare Biome enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final double temperature;"))
        assertTrue(content.contains("final double downfall;"))
        assertTrue(content.contains("final bool hasPrecipitation;"))
        assertTrue(content.contains("cherryGrove('Cherry Grove', 'minecraft:cherry_grove', 0.5, 0.8, true)"))
    }
}
