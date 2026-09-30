package net.theevilreaper.stelaris.cli.generator.dart.component

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DataComponentGeneratorTest : GenerationTestBase() {

    @Test
    fun `test data component generation`() {
        DataComponentGenerator().generate(generationPath)

        val folder = generationPath.resolve("component").toFile()
        assertTrue(folder.exists(), "Expected component package folder to exist")
        assertEquals(setOf("component_schema.dart", "data_components.dart"), folder.list()!!.toSet())

        val schema = folder.resolve("component_schema.dart").readText()
        assertTrue(schema.contains("sealed class ComponentSchema"))
        assertTrue(schema.contains("final class ComponentSpec"))

        val catalog = folder.resolve("data_components.dart").readText()
        assertTrue(catalog.contains("import 'component_schema.dart';"))
        assertTrue(catalog.contains("const List<ComponentSpec> dataComponents = ["))
        assertTrue(
            catalog.contains("ComponentSpec('minecraft:max_stack_size', 'MAX_STACK_SIZE', IntSchema(min: 1, max: 99)),")
        )
        assertTrue(
            catalog.contains(
                "ComponentSpec('minecraft:lore', 'LORE', ListSchema(TextSchema(), maxLength: 256), managed: true),"
            )
        )
    }
}
