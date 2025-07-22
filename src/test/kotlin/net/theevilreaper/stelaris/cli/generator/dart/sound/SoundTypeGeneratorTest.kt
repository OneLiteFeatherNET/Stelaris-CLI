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
            /// Generated class for the sound sources. Don't edit this file manually
            enum SoundType {

              block('Block', 'BLOCK'),
              entity('Entity', 'ENTITY'),
              music('Music', 'MUSIC'),
              item('Item', 'ITEM'),
              ambient('Ambient', 'AMBIENT');

              final String displayName;
              final String entry;

              SoundType(String displayName, String entry);

            }
        """.trimIndent()

        assertEquals(expectedClass, soundTypeFile.readText(), "Generated Dart class does not match expected content")
    }

}