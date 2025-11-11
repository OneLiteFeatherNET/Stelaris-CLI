package net.theevilreaper.stelaris.cli.generator.dart.bossbar

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files

class BossBarColorGeneratorTest : GenerationTestBase() {

    @Test
    fun `test boss bar color generation`() {
        val generator = BossBarColorGenerator()
        generator.generate(generationPath)
        val folder = generationPath.resolve("bossbar").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = generationPath.resolve("bossbar").toFile().listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val bossBarColorFile = generatedFiles.first()

        assertNotNull(bossBarColorFile)
        assertTrue(
            bossBarColorFile.name.contains("boss_bar_color"),
            "Expected generated file to contain 'boss_bar_color'"
        )
        assertTrue(
            bossBarColorFile.name.endsWith(".dart"),
            "Expected generated file to be a Dart file"
        )

        val expectedClass = """
            enum BossBarColor {

              pink('Pink', 'PINK'),
              blue('Blue', 'BLUE'),
              red('Red', 'RED'),
              green('Green', 'GREEN'),
              yellow('Yellow', 'YELLOW'),
              purple('Purple', 'PURPLE'),
              white('White', 'WHITE');

              final String displayName;
              final String type;

              const BossBarColor(this.displayName, this.type);

            }
        """.trimIndent()

        val content = Files.readString(bossBarColorFile.toPath())
        assertEquals(expectedClass, content, "Generated Dart class does not match expected content")
    }
}
