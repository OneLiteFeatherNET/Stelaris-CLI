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
            import '../../api/keyed.dart';

            final Map<String, ItemRarity> _itemRarityByKey = {for (final e in ItemRarity.values) e.key: e};

            enum ItemRarity implements Keyed {

              common('Common', 'common'),
              uncommon('Uncommon', 'uncommon'),
              rare('Rare', 'rare'),
              epic('Epic', 'epic');

              final String displayName;
              final String key;

              const ItemRarity(this.displayName, this.key);

              /// Returns the entry with the given [key] or null if there is none.
              static ItemRarity? byKey(String key) {
                return _itemRarityByKey[key];
              }
            }
        """.trimIndent()

        assertEquals(generated(expectedContent), generatedFile.readText(), "Generated Dart class does not match expected content")
    }
}
