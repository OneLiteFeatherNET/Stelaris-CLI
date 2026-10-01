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

        val materialsFolder = generationPath.resolve("material").toFile()
        assertTrue(materialsFolder.exists(), "Expected materials package folder to exist")

        val generatedFiles = materialsFolder.listFiles()
        assertNotNull(generatedFiles)
        assertEquals(8, generatedFiles!!.size, "Expected exactly 8 material files to be generated")

        val expectedFiles = mapOf(
            "block_material.dart" to "enum BlockMaterial",
            "armor_material.dart" to "enum ArmorMaterial",
            "tool_material.dart" to "enum ToolMaterial",
            "weapon_material.dart" to "enum WeaponMaterial",
            "food_material.dart" to "enum FoodMaterial",
            "dye_material.dart" to "enum DyeMaterial",
            "spawn_egg_material.dart" to "enum SpawnEggMaterial"
        )

        for ((fileName, expectedEnum) in expectedFiles) {
            val file = materialsFolder.resolve(fileName)
            assertTrue(file.exists(), "Expected $fileName to exist")
            val content = file.readText()
            assertTrue(content.contains(expectedEnum), "Expected $fileName to contain '$expectedEnum'")
            assertTrue(content.contains("final String displayName;"), "$fileName should declare displayName property")
            assertTrue(content.contains("final String key;"), "$fileName should declare key property")
            assertTrue(content.contains("final int maxStackSize;"), "$fileName should declare maxStackSize property")
        }
    }

    @Test
    fun `test material search generation`(env: Env) {
        MaterialGenerator().generate(generationPath)

        val file = generationPath.resolve("material").resolve("material_search.dart").toFile()
        assertTrue(file.exists(), "Expected material_search.dart to exist")
        val content = file.readText()

        assertTrue(content.contains("import '../../api/material_search.dart';"))
        assertTrue(content.contains("enum MaterialCategory implements SearchCategory"))
        assertTrue(content.contains("enum MaterialSearchEntry implements SearchableMaterial"))
        assertTrue(content.contains("final String key;"))
        assertTrue(content.contains("final List<String> terms;"))
        assertTrue(content.contains("final int categories;"))

        val block = 1 shl 0
        assertTrue(content.contains("block($block)"), "Expected the block category to use the first bit")
        assertTrue(
            content.contains("diamondSword('minecraft:diamond_sword', 'diamond sword', ['diamond', 'sword'],"),
            "Expected the diamond sword entry to contain its search key and terms"
        )
        assertTrue(
            content.contains("stick('minecraft:stick', 'stick', ['stick'], 0)"),
            "Expected materials without a category to be part of the search"
        )
        assertEquals(
            Material.values().size,
            content.lines().count { it.trimStart().contains("('minecraft:") },
            "Expected one search entry per material"
        )
    }
}
