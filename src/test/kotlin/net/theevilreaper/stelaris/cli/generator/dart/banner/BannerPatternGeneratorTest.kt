package net.theevilreaper.stelaris.cli.generator.dart.banner

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BannerPatternGeneratorTest : GenerationTestBase() {

    @Test
    fun `test banner pattern generation`() {
        val generator = BannerPatternGenerator()
        assertEquals("BannerPatternGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("banner").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("banner_pattern.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum BannerPattern"), "Generated file should declare BannerPattern enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final String assetId;"))
        assertTrue(content.contains("final String translationKey;"))
        assertTrue(content.contains("flower('Flower', 'minecraft:flower', 'minecraft:flower', 'block.minecraft.banner.flower')"))
    }
}
