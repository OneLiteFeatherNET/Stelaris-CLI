package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import net.minestom.testing.Env
import net.minestom.testing.extension.MicrotusExtension
import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MicrotusExtension::class)
class EnchantmentDataGeneratorTest : GenerationTestBase() {

    @Test
    fun `test enchantment data generation`(env: Env) {
        EnchantmentDataGenerator().generate(generationPath)

        val file = generationPath.resolve("enchantment").resolve("enchantment_data.dart").toFile()
        assertTrue(file.exists(), "Expected enchantment_data.dart to exist")
        val lines = file.readLines()

        assertTrue(lines.contains("import '../../api/enchantment_data.dart';"))
        assertFalse(lines.any { it.contains("class EnchantmentData") }, "The class belongs to the data repository")
        assertTrue(lines.contains("const Map<String, EnchantmentData> enchantmentData = {"))
        val sharpness = lines.single { it.trimStart().startsWith("'minecraft:sharpness':") }
        assertTrue(sharpness.contains("maxLevel: 5"))
        assertTrue(sharpness.contains("'minecraft:diamond_sword'"), "Sharpness should support swords")
        assertFalse(sharpness.contains("'minecraft:bow'"), "Sharpness should not support bows")
        assertTrue(sharpness.contains("'minecraft:smite'"), "Sharpness should exclude smite")
        assertFalse(sharpness.contains("exclusiveWith: {'minecraft:sharpness'"), "An enchantment should not exclude itself")

        val silkTouch = lines.single { it.trimStart().startsWith("'minecraft:silk_touch':") }
        assertTrue(silkTouch.contains("exclusiveWith: {'minecraft:fortune'}"))
    }
}
