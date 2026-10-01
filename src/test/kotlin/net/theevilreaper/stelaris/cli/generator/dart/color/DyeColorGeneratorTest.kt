package net.theevilreaper.stelaris.cli.generator.dart.color

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DyeColorGeneratorTest : GenerationTestBase() {

    @Test
    fun `test dye color generation`() {
        val generator = DyeColorGenerator()

        generator.generate(generationPath)

        val folder = generationPath.resolve("color").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals(
            "dye_color.dart",
            generatedFile.name,
            "Expected generated file to be named 'dye_color.dart'"
        )

        val expectedContent = """
            import '../../api/rgb_color.dart';

            enum DyeColor {

              white(RgbColor.fromRGB(0xf9fffe), RgbColor.fromRGB(0xffffff), RgbColor.fromRGB(0xf0f0f0), 8),
              orange(RgbColor.fromRGB(0xf9801d), RgbColor.fromRGB(0xff681f), RgbColor.fromRGB(0xeb8844), 15),
              magenta(RgbColor.fromRGB(0xc74ebd), RgbColor.fromRGB(0xff00ff), RgbColor.fromRGB(0xc354cd), 16),
              lightBlue(RgbColor.fromRGB(0x3ab3da), RgbColor.fromRGB(0x9ac0cd), RgbColor.fromRGB(0x6689d3), 17),
              yellow(RgbColor.fromRGB(0xfed83d), RgbColor.fromRGB(0xffff00), RgbColor.fromRGB(0xdecf2a), 18),
              lime(RgbColor.fromRGB(0x80c71f), RgbColor.fromRGB(0xbfff00), RgbColor.fromRGB(0x41cd34), 19),
              pink(RgbColor.fromRGB(0xf38baa), RgbColor.fromRGB(0xff69b4), RgbColor.fromRGB(0xd88198), 20),
              gray(RgbColor.fromRGB(0x474f52), RgbColor.fromRGB(0x808080), RgbColor.fromRGB(0x434343), 21),
              lightGray(RgbColor.fromRGB(0x9d9d97), RgbColor.fromRGB(0xd3d3d3), RgbColor.fromRGB(0xababab), 22),
              cyan(RgbColor.fromRGB(0x169c9c), RgbColor.fromRGB(0x00ffff), RgbColor.fromRGB(0x287697), 23),
              purple(RgbColor.fromRGB(0x8932b8), RgbColor.fromRGB(0xa020f0), RgbColor.fromRGB(0x7b2fbe), 24),
              blue(RgbColor.fromRGB(0x3c44aa), RgbColor.fromRGB(0x0000ff), RgbColor.fromRGB(0x253192), 25),
              brown(RgbColor.fromRGB(0x835432), RgbColor.fromRGB(0x8b4513), RgbColor.fromRGB(0x51301a), 26),
              green(RgbColor.fromRGB(0x5e7c16), RgbColor.fromRGB(0x00ff00), RgbColor.fromRGB(0x3b511a), 27),
              red(RgbColor.fromRGB(0xb02e26), RgbColor.fromRGB(0xff0000), RgbColor.fromRGB(0xb3312c), 28),
              black(RgbColor.fromRGB(0x1d1d21), RgbColor.fromRGB(0x000000), RgbColor.fromRGB(0x1e1b1b), 29);

              final RgbColor textureDiffuseColor;
              final RgbColor textColor;
              final RgbColor fireworkColor;
              final int mapColorId;

              const DyeColor(this.textureDiffuseColor, this.textColor, this.fireworkColor, this.mapColorId);

            }
        """.trimIndent()

        assertEquals(generated(expectedContent), generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
