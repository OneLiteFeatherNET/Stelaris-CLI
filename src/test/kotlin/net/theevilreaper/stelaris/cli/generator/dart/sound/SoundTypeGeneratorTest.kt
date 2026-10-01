package net.theevilreaper.stelaris.cli.generator.dart.sound

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SoundTypeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test sound type generation`() {
        val generator = SoundTypeGenerator()
        generator.generate(generationPath)

        val generatedFiles = generationPath.toFile().listFiles()
        assertNotNull(generatedFiles)

        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")
        val soundTypeFile = generatedFiles.first().resolve("sound_type.dart")

        assertNotNull(soundTypeFile)
        assertTrue(
            soundTypeFile.name.contains("sound_type"),
            "Expected generated file to contain 'sound_type'"
        )

        assertTrue(
            soundTypeFile.name.endsWith(".dart"),
            "Expected generated file to be a Dart file"
        )

        val expectedClass = """
            import '../../api/keyed.dart';

            final Map<String, SoundType> _soundTypeByKey = {for (final e in SoundType.values) e.key: e};

            enum SoundType implements Keyed {

              block('Block', 'block'),
              entity('Entity', 'entity'),
              music('Music', 'music'),
              item('Item', 'item'),
              ambient('Ambient', 'ambient');

              final String displayName;
              final String key;

              const SoundType(this.displayName, this.key);

              /// Returns the entry with the given [key] or null if there is none.
              static SoundType? byKey(String key) {
                return _soundTypeByKey[key];
              }
            }
        """.trimIndent()

        assertEquals(generated(expectedClass), soundTypeFile.readText(), "Generated Dart class does not match expected content")
    }

}