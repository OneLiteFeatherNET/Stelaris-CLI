package net.theevilreaper.stelaris.cli.generator.dart.item

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FireworkShapeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test firework shape generation`() {
        val generator = FireworkShapeGenerator()
        assertEquals("FireworkShapeGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("item").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals(
            "firework_shape.dart",
            generatedFile.name,
            "Expected generated file to be named 'firework_shape.dart'"
        )

        val expectedContent = """
            /// The file is generated. Don't change anything here
            enum FireworkShape {

              smallBall('Small Ball', 0),
              largeBall('Large Ball', 1),
              star('Star', 2),
              creeper('Creeper', 3),
              burst('Burst', 4);

              final String displayName;
              final int id;

              const FireworkShape(this.displayName, this.id);

            }
        """.trimIndent()

        assertEquals(expectedContent, generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
