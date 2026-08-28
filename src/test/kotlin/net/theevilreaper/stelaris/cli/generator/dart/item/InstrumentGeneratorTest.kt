package net.theevilreaper.stelaris.cli.generator.dart.item

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class InstrumentGeneratorTest : GenerationTestBase() {

    @Test
    fun `test instrument generation`() {
        val generator = InstrumentGenerator()
        assertEquals("InstrumentGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("item").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("instrument.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum Instrument"), "Generated file should declare Instrument enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final double range;"))
        assertTrue(content.contains("final double useDuration;"))
        assertTrue(content.contains("ponderGoatHorn('Ponder Goat Horn', 'minecraft:ponder_goat_horn', 256.0, 7.0)"))
    }
}
