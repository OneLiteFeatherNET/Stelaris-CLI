package net.theevilreaper.stelaris.cli.generator.dart.block

import net.minestom.server.instance.block.Block
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockTagCatalogTest {

    private lateinit var tags: Map<String, List<String>>

    @BeforeAll
    fun collectTags() {
        tags = BlockTagCatalog().tags()
    }

    @Test
    fun `test catalog contains every tag`() {
        assertEquals(Block.staticRegistry().tags().size, tags.size)
    }

    @Test
    fun `test tags and their blocks are sorted by key`() {
        assertEquals(tags.keys.sorted(), tags.keys.toList())
        tags.forEach { (tag, blocks) -> assertEquals(blocks.sorted(), blocks, tag) }
    }

    @Test
    fun `test tags hold known blocks only`() {
        val blocks = Block.values().map { it.key().asString() }.toSet()
        tags.forEach { (tag, members) -> assertTrue(blocks.containsAll(members), tag) }
    }

    @Test
    fun `test stairs tag`() {
        val stairs = tags.getValue("minecraft:stairs")
        assertTrue("minecraft:oak_stairs" in stairs)
        assertFalse("minecraft:oak_slab" in stairs)
    }

    @Test
    fun `test nested tags are resolved to blocks`() {
        val logs = tags.getValue("minecraft:logs")
        assertTrue("minecraft:oak_log" in logs)
        assertTrue(logs.none { it.startsWith("#") })
    }
}
