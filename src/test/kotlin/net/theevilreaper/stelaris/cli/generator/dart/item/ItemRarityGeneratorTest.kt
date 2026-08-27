package net.theevilreaper.stelaris.cli.generator.dart.item

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ItemRarityGeneratorTest : GenerationTestBase() {

    @Test
    fun `test item rarity generation`() {
        val generator = ItemRarityGenerator()
        assertEquals("ItemRarityGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("item").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()
        assertEquals(
            "item_rarity.dart",
            generatedFile.name,
            "Expected generated file to be named 'item_rarity.dart'"
        )

        val expectedContent = """
            /// The file is generated. Don't change anything here
            enum ItemRarity {

              common('Common'),
              uncommon('Uncommon'),
              rare('Rare'),
              epic('Epic');

              final String displayName;

              const ItemRarity(this.displayName);

            }
        """.trimIndent()

        assertEquals(expectedContent, generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
