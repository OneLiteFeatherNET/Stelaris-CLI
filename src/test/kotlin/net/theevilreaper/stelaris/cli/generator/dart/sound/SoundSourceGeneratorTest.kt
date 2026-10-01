package net.theevilreaper.stelaris.cli.generator.dart.sound

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SoundSourceGeneratorTest : GenerationTestBase() {

    @Test
    fun `test sound source generation`() {
        val generator = SoundSourceGenerator()
        generator.generate(generationPath)

        val generatedFiles = generationPath.toFile().listFiles()
        assertNotNull(generatedFiles)

        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")
        assertTrue(generatedFiles.first().isDirectory, "Expected generated file to be a directory")
        val soundSourceFile = generatedFiles.first().resolve("sound_source.dart")

        assertNotNull(soundSourceFile)

        println("Generated file: ${soundSourceFile.name}")
        assertTrue(
            soundSourceFile.name.contains("sound_source"),
            "Expected generated file to contain 'sound_source'"
        )

        assertTrue(
            soundSourceFile.name.endsWith(".dart"),
            "Expected generated file to be a Dart file"
        )

        val expectedClass = """
            import '../../api/keyed.dart';

            final Map<String, SoundSource> _soundSourceByKey = {for (final e in SoundSource.values) e.key: e};

            enum SoundSource implements Keyed {

              master('Master', 'master'),
              music('Music', 'music'),
              record('Record', 'record'),
              weather('Weather', 'weather'),
              block('Block', 'block'),
              hostile('Hostile', 'hostile'),
              neutral('Neutral', 'neutral'),
              player('Player', 'player'),
              ambient('Ambient', 'ambient'),
              voice('Voice', 'voice'),
              ui('Ui', 'ui');

              final String displayName;
              final String key;

              const SoundSource(this.displayName, this.key);

              /// Returns the entry with the given [key] or null if there is none.
              static SoundSource? byKey(String key) {
                return _soundSourceByKey[key];
              }
            }
        """.trimIndent()

        assertEquals(generated(expectedClass), soundSourceFile.readText(), "Generated Dart class does not match expected content")
    }

}