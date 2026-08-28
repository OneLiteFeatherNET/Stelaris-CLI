package net.theevilreaper.stelaris.cli.generator.dart

import net.minestom.server.item.Material
import net.minestom.testing.Env
import net.minestom.testing.extension.MicrotusExtension
import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MicrotusExtension::class)
class MaterialGeneratorTest : GenerationTestBase() {

    @Test
    fun `test material generation`(env: Env) {
        val generator = MaterialGenerator()

        generator.generate(generationPath)

        val materialsFolder = generationPath.resolve("materials").toFile()
        assertTrue(materialsFolder.exists(), "Expected materials package folder to exist")

        val generatedFiles = materialsFolder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(7, generatedFiles!!.size, "Expected exactly 7 material files to be generated")

        val expectedFiles = mapOf(
            "block_materials.dart" to "enum BlockMaterial",
            "armor_materials.dart" to "enum ArmorMaterial",
            "tool_materials.dart" to "enum ToolMaterial",
            "weapon_materials.dart" to "enum WeaponMaterial",
            "food_materials.dart" to "enum FoodMaterial",
            "dye_materials.dart" to "enum DyeMaterial",
            "spawn_egg_materials.dart" to "enum SpawnEggMaterial"
        )

        for ((fileName, expectedEnum) in expectedFiles) {
            val file = materialsFolder.resolve(fileName)
            assertTrue(file.exists(), "Expected $fileName to exist")
            val content = file.readText()
            assertTrue(content.contains(expectedEnum), "Expected $fileName to contain '$expectedEnum'")
            assertTrue(content.contains("final String displayName;"), "$fileName should declare displayName property")
            assertTrue(content.contains("final String material;"), "$fileName should declare material property")
            assertTrue(content.contains("final int maxStackSize;"), "$fileName should declare maxStackSize property")
        }
    }
}
