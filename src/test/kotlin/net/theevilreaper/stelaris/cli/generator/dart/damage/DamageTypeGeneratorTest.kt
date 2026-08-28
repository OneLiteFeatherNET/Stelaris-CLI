package net.theevilreaper.stelaris.cli.generator.dart.damage

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DamageTypeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test damage type generation`() {
        val generator = DamageTypeGenerator()
        assertEquals("DamageTypeGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("damage").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals("damage_type.dart", generatedFile.name)

        val content = generatedFile.readText()
        assertTrue(content.contains("enum DamageType"), "Generated file should declare DamageType enum")
        assertTrue(content.contains("final String displayName;"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final String messageId;"))
        assertTrue(content.contains("final double exhaustion;"))
        assertTrue(content.contains("inFire('In Fire', 'minecraft:in_fire', 'inFire', 0.1)"))
    }
}
