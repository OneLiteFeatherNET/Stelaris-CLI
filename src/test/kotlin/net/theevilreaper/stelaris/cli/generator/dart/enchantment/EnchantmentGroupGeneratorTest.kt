package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EnchantmentGroupGeneratorTest : GenerationTestBase() {

    @Test
    fun `test enchantment group enumeration generation`() {
        val enchantmentGroupGenerator = EnchantmentGroupGenerator()
        enchantmentGroupGenerator.generate(generationPath)

        val generatedFiles = generationPath.resolve("enchantment").toFile().listFiles()
        assertNotNull(generatedFiles)
        assertEquals(1, generatedFiles!!.size, "Expected exactly one file to be generated")

        val generatedFile = generatedFiles.first()

        assertEquals(
            "enchantment_group.dart",
            generatedFile.absoluteFile.name,
            "Expected generated file to be named 'enchantment_group.dart'"
        )

        assertEquals(
            generated("""
            /// Represents a category of enchantments based on their primary application
            ///
            /// Enchantments are grouped by the type of items they can be applied to,
            /// making it easier to filter and organize them by use case.
            import '../../api/keyed.dart';

            final Map<String, EnchantmentGroup> _enchantmentGroupByKey = {for (final e in
                EnchantmentGroup.values) e.key: e};

            enum EnchantmentGroup implements Keyed {

              armor('Armor', 'armor'),
              weapon('Weapon', 'weapon'),
              tool('Tool', 'tool'),
              meta('Meta', 'meta');

              final String displayName;
              final String key;

              const EnchantmentGroup(this.displayName, this.key);

              /// Returns the entry with the given [key] or null if there is none.
              static EnchantmentGroup? byKey(String key) {
                return _enchantmentGroupByKey[key];
              }
            }
        """.trimIndent()),
            generatedFile.readText(), "Generated Dart class does not match expected content"
        )
    }
}
