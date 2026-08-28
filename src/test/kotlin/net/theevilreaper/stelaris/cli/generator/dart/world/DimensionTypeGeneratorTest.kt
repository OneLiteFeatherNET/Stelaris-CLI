package net.theevilreaper.stelaris.cli.generator.dart.world

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DimensionTypeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test dimension type generation`() {
        val generator = DimensionTypeGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("world").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("dimension_type.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum DimensionType"), "Generated file should declare DimensionType enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final int minY;"))
        assertTrue(content.contains("final int height;"))
        assertTrue(content.contains("final int logicalHeight;"))
        assertTrue(content.contains("final bool hasSkylight;"))
        assertTrue(content.contains("final bool hasCeiling;"))
        assertTrue(content.contains("final double ambientLight;"))
        assertTrue(content.contains("overworld('Overworld', 'minecraft:overworld', -64, 384, 384, true, false, 0.0)"))
    }
}
