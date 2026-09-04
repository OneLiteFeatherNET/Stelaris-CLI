package net.theevilreaper.stelaris.cli.generator.dart.text

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HoverEventActionGeneratorTest : GenerationTestBase() {

    @Test
    fun `test hover event action generation`() {
        val generator = HoverEventActionGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("text").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals(
            "hover_event_action.dart",
            generatedFile.name,
            "Expected generated file to be named 'hover_event_action.dart'"
        )

        val expectedContent = """
            /// The file is generated. Don't change anything here
            enum HoverEventAction {

              showEntity('Show Entity', 'show_entity'),
              showItem('Show Item', 'show_item'),
              showText('Show Text', 'show_text');

              final String displayName;
              final String name;

              const HoverEventAction(this.displayName, this.name);

            }
        """.trimIndent()

        assertEquals(expectedContent, generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
