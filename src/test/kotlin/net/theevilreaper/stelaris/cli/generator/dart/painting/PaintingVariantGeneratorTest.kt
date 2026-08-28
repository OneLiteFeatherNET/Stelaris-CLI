package net.theevilreaper.stelaris.cli.generator.dart.painting

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PaintingVariantGeneratorTest : GenerationTestBase() {

    @Test
    fun `test painting variant generation`() {
        val generator = PaintingVariantGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("painting").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("painting_variant.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum PaintingVariant"), "Generated file should declare PaintingVariant enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final int width;"))
        assertTrue(content.contains("final int height;"))
        assertTrue(content.contains("final String assetId;"))
        assertTrue(content.contains("kebab('Kebab', 'minecraft:kebab', 1, 1, 'minecraft:kebab')"))
    }
}
