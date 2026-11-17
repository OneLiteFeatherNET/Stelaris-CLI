package net.theevilreaper.stelaris.cli.generator.dart.block

import com.google.auto.service.AutoService
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DartSource
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Path

/**
 * Generates the block tags of the game with the keys of the blocks each of them contains.
 *
 * The tags are written without the leading `#` which marks a tag reference in the data of the game.
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "BlockTagGenerator")
class BlockTagGenerator(
    private val catalog: BlockTagCatalog = BlockTagCatalog(),
) : BaseGenerator(
    className = "BlockTag",
    packageName = "block",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        writeGenerated(folder, "block_tags", source(catalog.tags()))
    }

    private fun source(tags: Map<String, List<String>>): String = buildString {
        appendLine("/// The block tags of the game with the keys of their blocks, both sorted by key.")
        appendLine("///")
        appendLine("/// A tag is written without the leading `#`, e.g. `minecraft:stairs`.")
        appendLine("const Map<String, List<String>> blockTags = {")
        tags.forEach { (tag, blocks) ->
            appendLine("  ${DartSource.string(tag)}: [${blocks.joinToString(", ", transform = DartSource::string)}],")
        }
        appendLine("};")
    }
}
