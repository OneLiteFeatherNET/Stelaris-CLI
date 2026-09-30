package net.theevilreaper.stelaris.cli.generator.dart

import net.minestom.server.MinecraftServer
import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VersionGeneratorTest : GenerationTestBase() {

    @Test
    fun `test version generation`() {
        VersionGenerator().generate(generationPath)

        val file = generationPath.resolve("version.dart").toFile()
        val expected = """
            // The file is generated. Don't change anything here

            /// The Minecraft version the data of the library belongs to.
            const String vulpesMinecraftVersion = '${MinecraftServer.VERSION_NAME}';
        """.trimIndent() + "\n"
        assertEquals(expected, file.readText())
    }
}
