package net.theevilreaper.stelaris.cli.generator.dart.item

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MapPostProcessingGeneratorTest : GenerationTestBase() {

    @Test
    fun `test map post processing generation`() {
        val generator = MapPostProcessingGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("item").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals(
            "map_post_processing.dart",
            generatedFile.name,
            "Expected generated file to be named 'map_post_processing.dart'"
        )

        val expectedContent = """
            import '../../api/keyed.dart';

            final Map<String, MapPostProcessing> _mapPostProcessingByKey = {for (final e in
                MapPostProcessing.values) e.key: e};

            enum MapPostProcessing implements Keyed {

              lock('Lock', 'lock'),
              scale('Scale', 'scale');

              final String displayName;
              final String key;

              const MapPostProcessing(this.displayName, this.key);

              /// Returns the entry with the given [key] or null if there is none.
              static MapPostProcessing? byKey(String key) {
                return _mapPostProcessingByKey[key];
              }
            }
        """.trimIndent()

        assertEquals(generated(expectedContent), generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
