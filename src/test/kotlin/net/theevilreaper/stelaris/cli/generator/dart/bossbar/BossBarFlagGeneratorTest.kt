package net.theevilreaper.stelaris.cli.generator.dart.bossbar

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BossBarFlagGeneratorTest : GenerationTestBase() {

    @Test
    fun `test boss bar flag generation`() {
        val generator = BossBarFlagGenerator()
        generator.generate(generationPath)

        val generatedFiles = generationPath.toFile().listFiles()
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

              progress('Progress', 'PROGRESS'),
              notched_6('Notched 6', 'NOTCHED_6'),
              notched_10('Notched 10', 'NOTCHED_10'),
              notched_12('Notched 12', 'NOTCHED_12'),
              notched_20('Notched 20', 'NOTCHED_20');

              final String displayName;
              final String type;

              const BossBarFlag(this.displayName, this.type);

            }
        """.trimIndent()

        val content = bossBarFlagFile.readText()
        assertEquals(expectedClass, content, "Generated Dart class does not match expected content")
    }
}
