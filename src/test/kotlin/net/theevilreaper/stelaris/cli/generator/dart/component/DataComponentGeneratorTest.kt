package net.theevilreaper.stelaris.cli.generator.dart.component

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import net.theevilreaper.stelaris.cli.generator.dart.util.GENERATED_FILE_HEADER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DataComponentGeneratorTest : GenerationTestBase() {

    @Test
    fun `test data component generation`() {
        DataComponentGenerator().generate(generationPath)

        val folder = generationPath.resolve("component").toFile()
        assertTrue(folder.exists(), "Expected component package folder to exist")
        assertEquals(setOf("data_components.dart"), folder.list()!!.toSet())

        val catalog = folder.resolve("data_components.dart").readText()
        assertTrue(catalog.startsWith(GENERATED_FILE_HEADER))
        assertTrue(catalog.contains("import '../../api/component_category.dart';"))
        assertTrue(catalog.contains("import '../../api/component_schema.dart';"))
        assertTrue(catalog.contains("const List<ComponentSpec> dataComponents = ["))
        assertTrue(
            catalog.contains("ComponentSpec('minecraft:max_stack_size', 'Max Stack Size', ComponentCategory.properties, " +
                "'MAX_STACK_SIZE', IntSchema(min: 1, max: 99)),")
        )
        assertTrue(
            catalog.contains(
                "ComponentSpec('minecraft:lore', 'Lore', ComponentCategory.display, 'LORE', " +
                    "ListSchema(TextSchema(), maxLength: 256), managed: true),"
            )
        )
    }
}
