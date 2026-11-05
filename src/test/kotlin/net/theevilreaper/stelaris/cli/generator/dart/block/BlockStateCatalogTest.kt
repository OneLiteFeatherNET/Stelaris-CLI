package net.theevilreaper.stelaris.cli.generator.dart.block

import net.minestom.server.instance.block.Block
import net.theevilreaper.stelaris.cli.generator.dart.component.BoolSchema
import net.theevilreaper.stelaris.cli.generator.dart.component.EnumSchema
import net.theevilreaper.stelaris.cli.generator.dart.component.IntSchema
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockStateCatalogTest {

    private val catalog = BlockStateCatalog()
    private lateinit var specs: Map<String, BlockStateSpec>

    @BeforeAll
    fun collectSpecs() {
        specs = catalog.specs().associateBy { it.key }
    }

    @Test
    fun `test catalog contains every block`() {
        assertEquals(Block.values().size, specs.size)
    }

    @Test
    fun `test catalog is sorted by key`() {
        val keys = catalog.specs().map { it.key }
        assertEquals(keys.sorted(), keys)
    }

    @Test
    fun `test pink petals properties`() {
        val spec = specs.getValue("minecraft:pink_petals")
        assertEquals("Pink Petals", spec.displayName)
        assertEquals(EnumSchema(listOf("north", "south", "west", "east")), spec.properties["facing"])
        assertEquals(IntSchema(min = 1, max = 4), spec.properties["flower_amount"])
        assertEquals(mapOf("facing" to "north", "flower_amount" to "1"), spec.defaults)
    }

    @Test
    fun `test block without properties`() {
        val spec = specs.getValue("minecraft:stone")
        assertTrue(spec.properties.isEmpty())
        assertTrue(spec.defaults.isEmpty())
    }

    @Test
    fun `test every default value is part of its property`() {
        specs.values.forEach { spec -> assertEquals(spec.properties.keys, spec.defaults.keys, spec.key) }
    }

    @Test
    fun `test schema derivation`() {
        assertEquals(BoolSchema, catalog.schemaOf(listOf("true", "false")))
        assertEquals(IntSchema(min = 0, max = 3), catalog.schemaOf(listOf("2", "0", "3", "1")))
        assertEquals(EnumSchema(listOf("1", "3")), catalog.schemaOf(listOf("1", "3")))
        assertEquals(EnumSchema(listOf("x", "y", "z")), catalog.schemaOf(listOf("x", "y", "z")))
    }
}
