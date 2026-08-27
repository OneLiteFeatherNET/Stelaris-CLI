package net.theevilreaper.stelaris.cli.generator.dart.entity.variant

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EntityVariantGeneratorTest : GenerationTestBase() {

    @Test
    fun `test entity variant generation`() {
        val generator = EntityVariantGenerator()
        assertEquals("EntityVariantGenerator", generator.getName())

        generator.generate(generationPath)

        val folder = generationPath.resolve("entity/variant").toFile()
        assertTrue(folder.exists(), "Expected generated folder to exist")
        val generatedFiles = folder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(6, generatedFiles!!.size, "Expected exactly 6 files to be generated")

        val fileNames = generatedFiles.map { it.name }.toSet()
        val expectedFileNames = setOf(
            "axolotl_variant.dart",
            "fox_variant.dart",
            "mooshroom_variant.dart",
            "parrot_variant.dart",
            "rabbit_variant.dart",
            "salmon_size.dart"
        )
        assertEquals(expectedFileNames, fileNames)

        // Check axolotl_variant.dart content
        val axolotlFile = folder.resolve("axolotl_variant.dart")
        val expectedAxolotlContent = """
            /// The file is generated. Don't change anything here
            enum AxolotlVariant {

              lucy('Lucy', 0),
              wild('Wild', 1),
              gold('Gold', 2),
              cyan('Cyan', 3),
              blue('Blue', 4);

              final String displayName;
              final int id;

              const AxolotlVariant(this.displayName, this.id);

            }
        """.trimIndent()
        assertEquals(expectedAxolotlContent, axolotlFile.readText())

        // Check mooshroom_variant.dart content
        val mooshroomFile = folder.resolve("mooshroom_variant.dart")
        val expectedMooshroomContent = """
            /// The file is generated. Don't change anything here
            enum MooshroomVariant {

              red('Red', 'red'),
              brown('Brown', 'brown');

              final String displayName;
              final String key;

              const MooshroomVariant(this.displayName, this.key);

            }
        """.trimIndent()
        // Check fox_variant.dart content
        val foxFile = folder.resolve("fox_variant.dart")
        val expectedFoxContent = """
            /// The file is generated. Don't change anything here
            enum FoxVariant {

              red('Red', 0),
              snow('Snow', 1);

              final String displayName;
              final int id;

              const FoxVariant(this.displayName, this.id);

            }
        """.trimIndent()
        assertEquals(expectedFoxContent, foxFile.readText())

        // Check parrot_variant.dart content
        val parrotFile = folder.resolve("parrot_variant.dart")
        val expectedParrotContent = """
            /// The file is generated. Don't change anything here
            enum ParrotVariant {

              redBlue('Red Blue', 0),
              blue('Blue', 1),
              green('Green', 2),
              yellowBlue('Yellow Blue', 3),
              grey('Grey', 4);

              final String displayName;
              final int id;

              const ParrotVariant(this.displayName, this.id);

            }
        """.trimIndent()
        assertEquals(expectedParrotContent, parrotFile.readText())

        // Check rabbit_variant.dart content
        val rabbitFile = folder.resolve("rabbit_variant.dart")
        val expectedRabbitContent = """
            /// The file is generated. Don't change anything here
            enum RabbitVariant {

              brown('Brown', 0),
              white('White', 1),
              black('Black', 2),
              blackAndWhite('Black And White', 3),
              gold('Gold', 4),
              saltAndPepper('Salt And Pepper', 5),
              killerBunny('Killer Bunny', 6);

              final String displayName;
              final int id;

              const RabbitVariant(this.displayName, this.id);

            }
        """.trimIndent()
        assertEquals(expectedRabbitContent, rabbitFile.readText())

        // Check salmon_size.dart content
        val salmonFile = folder.resolve("salmon_size.dart")
        val expectedSalmonContent = """
            /// The file is generated. Don't change anything here
            enum SalmonSize {

              small('Small', 0),
              medium('Medium', 1),
              large('Large', 2);

              final String displayName;
              final int id;

              const SalmonSize(this.displayName, this.id);

            }
        """.trimIndent()
        assertEquals(expectedSalmonContent, salmonFile.readText())
    }
}
