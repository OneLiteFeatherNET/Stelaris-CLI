package net.theevilreaper.stelaris.cli.generator.dart.block

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import net.theevilreaper.stelaris.cli.generator.dart.util.GENERATED_FILE_HEADER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BlockStateGeneratorTest : GenerationTestBase() {

    @Test
    fun `test block state generation`() {
        BlockStateGenerator().generate(generationPath)

        val folder = generationPath.resolve("block").toFile()
        assertEquals(setOf("block_states.dart"), folder.list()!!.toSet())

        val catalog = folder.resolve("block_states.dart").readText()
        assertTrue(catalog.startsWith(GENERATED_FILE_HEADER))
        assertTrue(catalog.contains("import '../../api/block_state_spec.dart';"))
        assertTrue(catalog.contains("import '../../api/component_schema.dart';"))
        assertTrue(catalog.contains("const List<BlockStateSpec> blockStates = ["))
        assertTrue(
            catalog.contains(
                "BlockStateSpec('minecraft:pink_petals', 'Pink Petals', " +
                    "{'facing': EnumSchema(['north', 'south', 'west', 'east']), 'flower_amount': IntSchema(min: 1, max: 4)}, " +
                    "{'facing': 'north', 'flower_amount': '1'}),"
            )
        )
        assertTrue(catalog.contains("BlockStateSpec('minecraft:stone', 'Stone', {}, {}),"))
    }
}
