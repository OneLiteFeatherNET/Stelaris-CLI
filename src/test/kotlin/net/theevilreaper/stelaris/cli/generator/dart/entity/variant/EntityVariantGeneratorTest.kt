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
            import '../../../api/keyed.dart';

            final Map<String, AxolotlVariant> _axolotlVariantByKey = {for (final e in AxolotlVariant.values)
                e.key: e};

            enum AxolotlVariant implements Keyed {

              lucy('Lucy', 'lucy', 0),
              wild('Wild', 'wild', 1),
              gold('Gold', 'gold', 2),
              cyan('Cyan', 'cyan', 3),
              blue('Blue', 'blue', 4);

              final String displayName;
              final String key;
              final int id;

              const AxolotlVariant(this.displayName, this.key, this.id);

              /// Returns the entry with the given [key] or null if there is none.
              static AxolotlVariant? byKey(String key) {
                return _axolotlVariantByKey[key];
              }
            }
        """.trimIndent()
        assertEquals(generated(expectedAxolotlContent), axolotlFile.readText())

        // Check mooshroom_variant.dart content
        val mooshroomFile = folder.resolve("mooshroom_variant.dart")
        val expectedMooshroomContent = """
            import '../../../api/keyed.dart';

            final Map<String, MooshroomVariant> _mooshroomVariantByKey = {for (final e in
                MooshroomVariant.values) e.key: e};

            enum MooshroomVariant implements Keyed {

              red('Red', 'red'),
              brown('Brown', 'brown');

              final String displayName;
              final String key;

              const MooshroomVariant(this.displayName, this.key);

              /// Returns the entry with the given [key] or null if there is none.
              static MooshroomVariant? byKey(String key) {
                return _mooshroomVariantByKey[key];
              }
            }
        """.trimIndent()
        // Check fox_variant.dart content
        val foxFile = folder.resolve("fox_variant.dart")
        val expectedFoxContent = """
            import '../../../api/keyed.dart';

            final Map<String, FoxVariant> _foxVariantByKey = {for (final e in FoxVariant.values) e.key: e};

            enum FoxVariant implements Keyed {

              red('Red', 'red', 0),
              snow('Snow', 'snow', 1);

              final String displayName;
              final String key;
              final int id;

              const FoxVariant(this.displayName, this.key, this.id);

              /// Returns the entry with the given [key] or null if there is none.
              static FoxVariant? byKey(String key) {
                return _foxVariantByKey[key];
              }
            }
        """.trimIndent()
        assertEquals(generated(expectedFoxContent), foxFile.readText())

        // Check parrot_variant.dart content
        val parrotFile = folder.resolve("parrot_variant.dart")
        val expectedParrotContent = """
            import '../../../api/keyed.dart';

            final Map<String, ParrotVariant> _parrotVariantByKey = {for (final e in ParrotVariant.values) e.key:
                e};

            enum ParrotVariant implements Keyed {

              redBlue('Red Blue', 'red_blue', 0),
              blue('Blue', 'blue', 1),
              green('Green', 'green', 2),
              yellowBlue('Yellow Blue', 'yellow_blue', 3),
              grey('Grey', 'grey', 4);

              final String displayName;
              final String key;
              final int id;

              const ParrotVariant(this.displayName, this.key, this.id);

              /// Returns the entry with the given [key] or null if there is none.
              static ParrotVariant? byKey(String key) {
                return _parrotVariantByKey[key];
              }
            }
        """.trimIndent()
        assertEquals(generated(expectedParrotContent), parrotFile.readText())

        // Check rabbit_variant.dart content
        val rabbitFile = folder.resolve("rabbit_variant.dart")
        val expectedRabbitContent = """
            import '../../../api/keyed.dart';

            final Map<String, RabbitVariant> _rabbitVariantByKey = {for (final e in RabbitVariant.values) e.key:
                e};

            enum RabbitVariant implements Keyed {

              brown('Brown', 'brown', 0),
              white('White', 'white', 1),
              black('Black', 'black', 2),
              blackAndWhite('Black And White', 'black_and_white', 3),
              gold('Gold', 'gold', 4),
              saltAndPepper('Salt And Pepper', 'salt_and_pepper', 5),
              killerBunny('Killer Bunny', 'killer_bunny', 6);

              final String displayName;
              final String key;
              final int id;

              const RabbitVariant(this.displayName, this.key, this.id);

              /// Returns the entry with the given [key] or null if there is none.
              static RabbitVariant? byKey(String key) {
                return _rabbitVariantByKey[key];
              }
            }
        """.trimIndent()
        assertEquals(generated(expectedRabbitContent), rabbitFile.readText())

        // Check salmon_size.dart content
        val salmonFile = folder.resolve("salmon_size.dart")
        val expectedSalmonContent = """
            import '../../../api/keyed.dart';

            final Map<String, SalmonSize> _salmonSizeByKey = {for (final e in SalmonSize.values) e.key: e};

            enum SalmonSize implements Keyed {

              small('Small', 'small', 0),
              medium('Medium', 'medium', 1),
              large('Large', 'large', 2);

              final String displayName;
              final String key;
              final int id;

              const SalmonSize(this.displayName, this.key, this.id);

              /// Returns the entry with the given [key] or null if there is none.
              static SalmonSize? byKey(String key) {
                return _salmonSizeByKey[key];
              }
            }
        """.trimIndent()
        assertEquals(generated(expectedSalmonContent), salmonFile.readText())
    }
}
