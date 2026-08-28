package net.theevilreaper.stelaris.cli.generator.dart.villager

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class VillagerTypeGeneratorTest : GenerationTestBase() {

    @Test
    fun `test villager type generation`() {
        val generator = VillagerTypeGenerator()
        assertEquals("VillagerTypeGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("villager").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals(
            "villager_type.dart",
            generatedFile.name,
            "Expected generated file to be named 'villager_type.dart'"
        )

        val expectedContent = """
            /// The file is generated. Don't change anything here
            enum VillagerType {

              desert('Desert', 'minecraft:desert'),
              jungle('Jungle', 'minecraft:jungle'),
              plains('Plains', 'minecraft:plains'),
              savanna('Savanna', 'minecraft:savanna'),
              snow('Snow', 'minecraft:snow'),
              swamp('Swamp', 'minecraft:swamp'),
              taiga('Taiga', 'minecraft:taiga');

              final String displayName;
              final String key;

              const VillagerType(this.displayName, this.key);

            }
        """.trimIndent()

        assertEquals(expectedContent, generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
