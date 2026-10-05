package net.theevilreaper.stelaris.cli.generator.dart

import net.minestom.server.MinecraftServer
import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VersionGeneratorTest : GenerationTestBase() {

    @Test
    fun `test version generation`() {
        VersionGenerator().generate(generationPath)

        val file = generationPath.resolve("version").resolve("version.dart").toFile()
        val expected = generated(
            """
            /// The Minecraft version the data of the library belongs to.
            const String vulpesMinecraftVersion = '${MinecraftServer.VERSION_NAME}';
            """.trimIndent()
        )
        assertEquals(expected, file.readText())
    }
}
