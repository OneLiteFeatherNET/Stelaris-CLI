package net.theevilreaper.stelaris.cli.generator.dart

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EntityTypeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test entity type generation`() {
        val generator = EntityTypeGenerator()
        assertEquals("EntityTypeGenerator", generator.getName())

        generator.generate(generationPath)

        val generatedFiles = generationPath.toFile().listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val file = generatedFiles.first()
        assertTrue(file.name == "entity_type.dart", "Expected file name to be entity_type.dart")
        val content = file.readText()
        assertTrue(content.contains("enum EntityType"), "Generated file should contain enum EntityType")
        assertTrue(content.contains("final String displayName;"), "Generated file should declare displayName property")
        assertTrue(content.contains("final String type;"), "Generated file should declare type property")
    }
}
