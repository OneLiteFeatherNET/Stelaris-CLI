package net.theevilreaper.stelaris.cli.generator.dart.block

import net.theevilreaper.stelaris.cli.generator.GenerationTestBase
import net.theevilreaper.stelaris.cli.generator.dart.util.GENERATED_FILE_HEADER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BlockTagGeneratorTest : GenerationTestBase() {

    @Test
    fun `test block tag generation`() {
        BlockTagGenerator().generate(generationPath)

        val folder = generationPath.resolve("block").toFile()
        assertEquals(setOf("block_tags.dart"), folder.list()!!.toSet())

        val catalog = folder.resolve("block_tags.dart").readText()
        assertTrue(catalog.startsWith(GENERATED_FILE_HEADER))
        assertTrue(catalog.contains("const Map<String, List<String>> blockTags = {"))
        assertTrue(catalog.contains("  'minecraft:rails': ['minecraft:activator_rail', 'minecraft:detector_rail', 'minecraft:powered_rail', 'minecraft:rail'],"))
        assertTrue(catalog.endsWith("],\n};\n"))
    }
}
