package net.theevilreaper.stelaris.cli.generator.dart.item

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TrimPatternGeneratorTest : GenerationTestBase() {

    @Test
    fun `test trim pattern generation`() {
        val generator = TrimPatternGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("item").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("trim_pattern.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum TrimPattern"), "Generated file should declare TrimPattern enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final String assetId;"))
        assertTrue(content.contains("final bool decal;"))
        assertTrue(content.contains("sentry('Sentry', 'minecraft:sentry', 'minecraft:sentry', false)"))
    }
}
