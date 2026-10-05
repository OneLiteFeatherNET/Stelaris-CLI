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
        assertEquals(
            setOf("component_category.dart", "component_schema.dart", "data_components.dart"),
            folder.list()!!.toSet()
        )

        val schema = folder.resolve("component_schema.dart").readText()
        assertTrue(schema.startsWith(GENERATED_FILE_HEADER))
        assertTrue(schema.contains("sealed class ComponentSchema"))
        assertTrue(schema.contains("import '../../api/keyed.dart';"))
        assertTrue(schema.contains("final class ComponentSpec implements Keyed"))

        val category = folder.resolve("component_category.dart").readText()
        assertTrue(category.startsWith(GENERATED_FILE_HEADER))
        assertTrue(category.contains("enum ComponentCategory implements Keyed {"))
        assertTrue(category.contains("entityVariant('Entity Variant', 'entity_variant'),"))
        assertTrue(category.contains("static ComponentCategory? byKey(String key)"))

        val catalog = folder.resolve("data_components.dart").readText()
        assertTrue(catalog.startsWith(GENERATED_FILE_HEADER))
        assertTrue(catalog.contains("import 'component_category.dart';"))
        assertTrue(catalog.contains("import 'component_schema.dart';"))
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
