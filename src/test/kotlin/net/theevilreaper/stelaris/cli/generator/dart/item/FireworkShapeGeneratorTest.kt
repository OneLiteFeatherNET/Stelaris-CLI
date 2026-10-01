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
            import '../../api/keyed.dart';

            final Map<String, FireworkShape> _fireworkShapeByKey = {for (final e in FireworkShape.values) e.key:
                e};

            enum FireworkShape implements Keyed {

              smallBall('Small Ball', 'small_ball', 0),
              largeBall('Large Ball', 'large_ball', 1),
              star('Star', 'star', 2),
              creeper('Creeper', 'creeper', 3),
              burst('Burst', 'burst', 4);

              final String displayName;
              final String key;
              final int id;

              const FireworkShape(this.displayName, this.key, this.id);

              /// Returns the entry with the given [key] or null if there is none.
              static FireworkShape? byKey(String key) {
                return _fireworkShapeByKey[key];
              }
            }
        """.trimIndent()

        assertEquals(generated(expectedContent), generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
