package net.theevilreaper.stelaris.cli.generator.dart.bossbar

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BossBarFlagGeneratorTest : GenerationTestBase() {

    @Test
    fun `test boss bar flag generation`() {
        val generator = BossBarFlagGenerator()
        generator.generate(generationPath)

        val folder = generationPath.resolve("bossbar").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = generationPath.resolve("bossbar").toFile().listFiles()

        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val bossBarFlagFile = generatedFiles.first()

        assertNotNull(bossBarFlagFile)
        assertTrue(
            bossBarFlagFile.name.contains("boss_bar_flag"),
            "Expected generated file to contain 'boss_bar_flag'"
        )
        assertTrue(
            bossBarFlagFile.name.endsWith(".dart"),
            "Expected generated file to be a Dart file"
        )

        val expectedClass = """
            enum BossBarFlag {

              darkenScreen('Darken Screen', 'darken_screen'),
              playBossMusic('Play Boss Music', 'play_boss_music'),
              createWorldFog('Create World Fog', 'create_world_fog');

              final String displayName;
              final String key;

              const BossBarFlag(this.displayName, this.key);

            }
        """.trimIndent()

        val content = bossBarFlagFile.readText()
        assertEquals(generated(expectedClass), content, "Generated Dart class does not match expected content")
    }
}
