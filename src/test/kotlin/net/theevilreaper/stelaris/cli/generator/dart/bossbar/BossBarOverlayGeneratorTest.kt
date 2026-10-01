package net.theevilreaper.stelaris.cli.generator.dart.bossbar

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BossBarOverlayGeneratorTest : GenerationTestBase() {

    @Test
    fun `test boss bar overlay generation`() {
        val generator = BossBarOverlayGenerator()
        generator.generate(generationPath)

        val folder = generationPath.resolve("bossbar").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = generationPath.resolve("bossbar").toFile().listFiles()

        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val bossBarOverlayFile = generatedFiles.first()

        assertNotNull(bossBarOverlayFile)
        assertTrue(
            bossBarOverlayFile.name.contains("boss_bar_overlay"),
            "Expected generated file to contain 'boss_bar_overlay'"
        )
        assertTrue(
            bossBarOverlayFile.name.endsWith(".dart"),
            "Expected generated file to be a Dart file"
        )

        val expectedClass = """
            import '../../api/keyed.dart';

            final Map<String, BossBarOverlay> _bossBarOverlayByKey = {for (final e in BossBarOverlay.values)
                e.key: e};

            enum BossBarOverlay implements Keyed {

              progress('Progress', 'progress'),
              notched6('Notched 6', 'notched_6'),
              notched10('Notched 10', 'notched_10'),
              notched12('Notched 12', 'notched_12'),
              notched20('Notched 20', 'notched_20');

              final String displayName;
              final String key;

              const BossBarOverlay(this.displayName, this.key);

              /// Returns the entry with the given [key] or null if there is none.
              static BossBarOverlay? byKey(String key) {
                return _bossBarOverlayByKey[key];
              }
            }
        """.trimIndent()

        val content = bossBarOverlayFile.readText()
        assertEquals(generated(expectedClass), content, "Generated Dart class does not match expected content")
    }

}