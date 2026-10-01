package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.stelaris.cli.generator.dart.util.GENERATED_FILE_HEADER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class DartLibraryTest {

    @TempDir
    lateinit var projectPath: Path

    @Test
    fun `test clean removes only generated files`() {
        val lib = projectPath.resolve("lib")
        val staleFile = lib.resolve("src/generated/old/old.dart")
        val staleLibrary = lib.resolve("old.dart")
        val handwrittenLibrary = lib.resolve("handwritten.dart")
        val apiFile = lib.resolve("src/api/rgb_color.dart")
        Files.createDirectories(staleFile.parent)
        Files.createDirectories(apiFile.parent)
        Files.writeString(staleFile, "$GENERATED_FILE_HEADER\n")
        Files.writeString(staleLibrary, "$GENERATED_FILE_HEADER\n\nexport 'src/generated/old/old.dart';\n")
        Files.writeString(handwrittenLibrary, "export 'src/api/rgb_color.dart';\n")
        Files.writeString(apiFile, "class RgbColor {}\n")

        val library = DartLibrary(projectPath)
        library.clean()

        assertTrue(Files.isDirectory(library.generatedFolder), "Expected the generated folder to exist")
        assertFalse(Files.exists(staleFile), "Expected the old generated file to be removed")
        assertFalse(Files.exists(staleLibrary), "Expected the old generated library to be removed")
        assertTrue(Files.exists(handwrittenLibrary), "Expected the handwritten library to be kept")
        assertTrue(Files.exists(apiFile), "Expected the api file to be kept")
    }

    @Test
    fun `test write libraries`() {
        Files.writeString(projectPath.resolve("pubspec.yaml"), "name: test_data\nversion: 1.0.0\n")
        val lib = projectPath.resolve("lib")
        Files.createDirectories(lib.resolve("src/api"))
        Files.writeString(lib.resolve("src/api/rgb_color.dart"), "class RgbColor {}\n")
        Files.writeString(lib.resolve("src/api/enchantment.dart"), "abstract class Enchantment {}\n")

        val library = DartLibrary(projectPath)
        library.clean()
        val color = library.generatedFolder.resolve("color")
        Files.createDirectories(color)
        Files.writeString(color.resolve("dye_color.dart"), "import '../../api/rgb_color.dart';\n\nenum DyeColor { white }\n")
        val world = library.generatedFolder.resolve("world")
        Files.createDirectories(world.resolve("nested"))
        Files.writeString(world.resolve("biome.dart"), "enum Biome { plains }\n")
        Files.writeString(world.resolve("nested/game_mode.dart"), "enum GameMode { survival }\n")

        library.writeLibraries()

        assertEquals(
            expectedLibrary(
                "export 'src/api/rgb_color.dart';",
                "export 'src/generated/color/dye_color.dart';"
            ),
            Files.readString(lib.resolve("color.dart"))
        )
        assertEquals(
            expectedLibrary(
                "export 'src/generated/world/biome.dart';",
                "export 'src/generated/world/nested/game_mode.dart';"
            ),
            Files.readString(lib.resolve("world.dart"))
        )
        assertEquals(
            expectedLibrary(
                "export 'color.dart';",
                "export 'src/api/enchantment.dart';",
                "export 'src/api/rgb_color.dart';",
                "export 'world.dart';"
            ),
            Files.readString(lib.resolve("test_data.dart"))
        )
    }

    private fun expectedLibrary(vararg exports: String): String =
        "$GENERATED_FILE_HEADER\n\n${exports.joinToString("\n")}\n"
}
