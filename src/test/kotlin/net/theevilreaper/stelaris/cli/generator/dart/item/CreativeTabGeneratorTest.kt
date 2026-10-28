package net.theevilreaper.stelaris.cli.generator.dart.item

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CreativeTabGeneratorTest : GenerationTestBase() {

    @Test
    fun `test creative tab enumeration generation`() {
        val creativeTabGenerator = CreativeTabGenerator()
        creativeTabGenerator.generate(generationPath)

        val generatedFiles = generationPath.resolve("item").toFile().listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()

        assertEquals(
            "creative_tab.dart",
            generatedFile.absoluteFile.name,
            "Expected generated file to be named 'creative_tab.dart'"
        )

        assertEquals(
            generated("""
            /// Represents the item groups of the creative inventory
            import '../../api/keyed.dart';

            final Map<String, CreativeTab> _creativeTabByKey = {for (final e in CreativeTab.values) e.key: e};

            enum CreativeTab implements Keyed {
              building_blocks('Building Blocks', 'building_blocks'),
              colored_blocks('Colored Blocks', 'colored_blocks'),
              natural_blocks('Natural Blocks', 'natural_blocks'),
              functional_blocks('Functional Blocks', 'functional_blocks'),
              redstone_blocks('Redstone Blocks', 'redstone_blocks'),
              tools_and_utilities('Tools & Utilities', 'tools_and_utilities'),
              combat('Combat', 'combat'),
              food_and_drinks('Food & Drinks', 'food_and_drinks'),
              ingredients('Ingredients', 'ingredients'),
              spawn_eggs('Spawn Eggs', 'spawn_eggs'),
              op_blocks('Operator Utilities', 'op_blocks');

              final String displayName;
              final String key;

              const CreativeTab(this.displayName, this.key);

              /// Returns the entry with the given [key] or null if there is none.
              static CreativeTab? byKey(String key) {
                return _creativeTabByKey[key];
              }
            }
        """.trimIndent()),
            generatedFile.readText(), "Generated Dart class does not match expected content"
        )
    }
}
