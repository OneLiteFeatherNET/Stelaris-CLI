package net.theevilreaper.stelaris.cli.generator.dart.item

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TrimMaterialGeneratorTest : GenerationTestBase() {

    @Test
    fun `test trim material generation`() {
        val generator = TrimMaterialGenerator()
        assertEquals("TrimMaterialGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("item").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("trim_material.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum TrimMaterial"), "Generated file should declare TrimMaterial enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final String assetId;"))
        assertTrue(content.contains("diamond('Diamond', 'minecraft:diamond', 'diamond')"))
    }
}
