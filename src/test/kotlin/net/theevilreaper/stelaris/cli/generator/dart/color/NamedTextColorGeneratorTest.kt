package net.theevilreaper.stelaris.cli.generator.dart.color

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NamedTextColorGeneratorTest : GenerationTestBase() {

    @Test
    fun `test named text color generation`() {
        val generator = NamedTextColorGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("color").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals(
            "named_text_color.dart",
            generatedFile.name,
            "Expected generated file to be named 'named_text_color.dart'"
        )

        val expectedContent = """
            import '../../api/keyed.dart';
            import '../../api/rgb_color.dart';

            final Map<String, NamedTextColor> _namedTextColorByKey = {for (final e in NamedTextColor.values)
                e.key: e};

            enum NamedTextColor implements Keyed {

              aqua('Aqua', 'aqua', RgbColor.fromRGB(0x55ffff)),
              black('Black', 'black', RgbColor.fromRGB(0x000000)),
              blue('Blue', 'blue', RgbColor.fromRGB(0x5555ff)),
              darkAqua('Dark Aqua', 'dark_aqua', RgbColor.fromRGB(0x00aaaa)),
              darkBlue('Dark Blue', 'dark_blue', RgbColor.fromRGB(0x0000aa)),
              darkGray('Dark Gray', 'dark_gray', RgbColor.fromRGB(0x555555)),
              darkGreen('Dark Green', 'dark_green', RgbColor.fromRGB(0x00aa00)),
              darkPurple('Dark Purple', 'dark_purple', RgbColor.fromRGB(0xaa00aa)),
              darkRed('Dark Red', 'dark_red', RgbColor.fromRGB(0xaa0000)),
              gold('Gold', 'gold', RgbColor.fromRGB(0xffaa00)),
              gray('Gray', 'gray', RgbColor.fromRGB(0xaaaaaa)),
              green('Green', 'green', RgbColor.fromRGB(0x55ff55)),
              lightPurple('Light Purple', 'light_purple', RgbColor.fromRGB(0xff55ff)),
              red('Red', 'red', RgbColor.fromRGB(0xff5555)),
              white('White', 'white', RgbColor.fromRGB(0xffffff)),
              yellow('Yellow', 'yellow', RgbColor.fromRGB(0xffff55));

              final String displayName;
              final String key;
              final RgbColor color;

              const NamedTextColor(this.displayName, this.key, this.color);

              /// Returns the entry with the given [key] or null if there is none.
              static NamedTextColor? byKey(String key) {
                return _namedTextColorByKey[key];
              }
            }
        """.trimIndent()

        assertEquals(generated(expectedContent), generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
