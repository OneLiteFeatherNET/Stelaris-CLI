package net.theevilreaper.stelaris.cli.generator.dart.component

import net.minestom.server.component.DataComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataComponentCatalogTest {

    private lateinit var specs: Map<String, ComponentSpec>

    @BeforeAll
    fun collectSpecs() {
        specs = DataComponentCatalog().specs().associateBy { it.key }
    }

    @Test
    fun `test catalog contains every data component`() {
        assertEquals(DataComponent.values().size, specs.size)
    }

    @Test
    fun `test catalog is sorted by key`() {
        val keys = DataComponentCatalog().specs().map { it.key }
        assertEquals(keys.sorted(), keys)
    }

    @Test
    fun `test max stack size is bounded`() {
        val spec = specs.getValue("minecraft:max_stack_size")
        assertEquals("MAX_STACK_SIZE", spec.javaField)
        assertEquals(IntSchema(min = 1, max = 99), spec.schema)
        assertFalse(spec.managed)
        assertTrue(spec.editable)
    }

    @Test
    fun `test lore is managed`() {
        val spec = specs.getValue("minecraft:lore")
        assertTrue(spec.managed)
        assertEquals(ListSchema(TextSchema, maxLength = 256), spec.schema)
    }

    @Test
    fun `test runtime state is not editable`() {
        assertFalse(specs.getValue("minecraft:bundle_contents").editable)
        assertFalse(specs.getValue("minecraft:container").editable)
    }

    @Test
    fun `test food uses vanilla field names`() {
        val food = specs.getValue("minecraft:food").schema as ObjectSchema
        assertEquals(setOf("nutrition", "saturation", "can_always_eat"), food.fields.keys)
        assertTrue(food.fields.getValue("can_always_eat").optional)
    }

    @Test
    fun `test overrides only name existing components`() {
        val overrides = ComponentOverrides.VANILLA
        val overriddenKeys = overrides.managed + overrides.nonEditable + overrides.componentSchemas.keys
        val unknownKeys = overriddenKeys - specs.keys
        assertTrue(unknownKeys.isEmpty(), "Overrides for unknown components: $unknownKeys")
    }
}
