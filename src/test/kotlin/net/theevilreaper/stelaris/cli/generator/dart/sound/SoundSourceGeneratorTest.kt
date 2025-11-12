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
            enum SoundSource {

              MASTER('Master', 'MASTER'),
              MUSIC('Music', 'MUSIC'),
              RECORD('Record', 'RECORD'),
              WEATHER('Weather', 'WEATHER'),
              BLOCK('Block', 'BLOCK'),
              HOSTILE('Hostile', 'HOSTILE'),
              NEUTRAL('Neutral', 'NEUTRAL'),
              PLAYER('Player', 'PLAYER'),
              AMBIENT('Ambient', 'AMBIENT'),
              VOICE('Voice', 'VOICE'),
              UI('Ui', 'UI');

              final String displayName;
              final String entry;

              const SoundSource(this.displayName, this.entry);

            }
        """.trimIndent()

        assertEquals(expectedClass, soundSourceFile.readText(), "Generated Dart class does not match expected content")
    }

}