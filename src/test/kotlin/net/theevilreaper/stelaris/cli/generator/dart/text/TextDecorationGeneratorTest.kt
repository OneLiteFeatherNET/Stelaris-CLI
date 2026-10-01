package net.theevilreaper.stelaris.cli.generator.dart.text

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TextDecorationGeneratorTest : GenerationTestBase() {

    @Test
    fun `test text decoration generation`() {
        val generator = TextDecorationGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("text").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals(
            "text_decoration.dart",
            generatedFile.name,
            "Expected generated file to be named 'text_decoration.dart'"
        )

        val expectedContent = """
            enum TextDecoration {

              bold('Bold', 'bold'),
              italic('Italic', 'italic'),
              obfuscated('Obfuscated', 'obfuscated'),
              strikethrough('Strikethrough', 'strikethrough'),
              underlined('Underlined', 'underlined');

              final String displayName;
              final String key;

              const TextDecoration(this.displayName, this.key);

            }
        """.trimIndent()

        assertEquals(generated(expectedContent), generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
