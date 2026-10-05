package net.theevilreaper.stelaris.cli.generator.dart.material

import net.minestom.testing.Env
import net.minestom.testing.extension.MicrotusExtension
import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MicrotusExtension::class)
class MaterialComponentsGeneratorTest : GenerationTestBase() {

    @Test
    fun `test material components generation`(env: Env) {
        MaterialComponentsGenerator().generate(generationPath)

        val file = generationPath.resolve("material").resolve("material_components.dart").toFile()
        assertTrue(file.exists(), "Expected material_components.dart to exist")
        val lines = file.readLines()

        val base = lines.single { it.startsWith("const List<String> baseMaterialComponents = [") }
        assertTrue(base.contains("'minecraft:max_stack_size'"), "Every material has a stack size")
        assertFalse(base.contains("'minecraft:food'"), "Not every material is food")

        assertTrue(lines.contains("const Map<String, List<String>> materialComponents = {"))
        val apple = lines.single { it.trimStart().startsWith("'minecraft:apple':") }
        assertTrue(apple.contains("'minecraft:food'"), "An apple should be food")
        assertFalse(apple.contains("'minecraft:max_stack_size'"), "Base components are not repeated")
        assertFalse(apple.contains("'minecraft:tool'"), "An apple should not be a tool")
        val pickaxe = lines.single { it.trimStart().startsWith("'minecraft:diamond_pickaxe':") }
        assertTrue(pickaxe.contains("'minecraft:tool'"))

        assertTrue(lines.any { it.startsWith("List<String> defaultComponentsOf(String key)") })
    }
}
