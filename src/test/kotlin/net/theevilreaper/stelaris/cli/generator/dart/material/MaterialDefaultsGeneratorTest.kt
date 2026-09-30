package net.theevilreaper.stelaris.cli.generator.dart.material

import net.minestom.testing.Env
import net.minestom.testing.extension.MicrotusExtension
import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MicrotusExtension::class)
class MaterialDefaultsGeneratorTest : GenerationTestBase() {

    @Test
    fun `test material defaults generation`(env: Env) {
        MaterialDefaultsGenerator().generate(generationPath)

        val file = generationPath.resolve("materials").resolve("material_defaults.dart").toFile()
        assertTrue(file.exists(), "Expected material_defaults.dart to exist")
        val content = file.readText()

        assertTrue(content.contains("final class MaterialDefaults {"))
        assertTrue(content.contains("const Map<String, MaterialDefaults> materialDefaults = {"))
        // Materials which are missing in the category enums
        assertTrue(content.contains("'minecraft:stick': MaterialDefaults(maxStackSize: 64, rarity: 'common'),"))
        assertTrue(content.contains("'minecraft:bow': MaterialDefaults(maxStackSize: 1, maxDamage: 384, rarity: 'common'),"))
        assertTrue(content.contains("'minecraft:compass': MaterialDefaults(maxStackSize: 64, rarity: 'common'),"))
        assertTrue(content.contains("'minecraft:wooden_sword': MaterialDefaults(maxStackSize: 1, maxDamage: 59, rarity: 'common'),"))
    }
}
