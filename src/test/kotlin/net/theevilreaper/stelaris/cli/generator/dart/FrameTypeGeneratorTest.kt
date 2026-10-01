package net.theevilreaper.stelaris.cli.generator.dart

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FrameTypeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test frame type generation`() {
        val generator = FrameTypeGenerator()

        generator.generate(generationPath)

        val generatedFiles = generationPath.resolve("advancement").toFile().listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val file = generatedFiles.first()
        assertTrue(file.name == "frame_type.dart", "Expected file name to be frame_type.dart")
        val content = file.readText()
        assertTrue(content.contains("enum FrameType"), "Generated file should contain enum FrameType")
        assertTrue(content.contains("final String displayName;"), "Generated file should declare displayName property")
    }
}
