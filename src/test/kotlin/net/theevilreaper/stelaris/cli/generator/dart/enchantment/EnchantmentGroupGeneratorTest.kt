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
            """
            /// Represents a category of enchantments based on their primary application
            ///
            /// Enchantments are grouped by the type of items they can be applied to,
            /// making it easier to filter and organize them by use case.
            enum EnchantmentGroup {

              armor('Armor'),
              weapon('Weapon'),
              tool('Tool'),
              meta('Meta');

              final String displayName;

              const EnchantmentGroup(this.displayName);

            }
        """.trimIndent(),
            generatedFile.readText(), "Generated Dart class does not match expected content"
        )
    }
}
