package net.theevilreaper.stelaris.cli.generator.dart.block

import com.google.auto.service.AutoService
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Path

/**
 * Generates the catalog of the state properties of all blocks.
 *
 * The [BlockStateSpec] class lives in `lib/src/api` of the data repository and uses the schema classes
 * of the data components, only the catalog is generated.
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "BlockStateGenerator")
class BlockStateGenerator(
    private val catalog: BlockStateCatalog = BlockStateCatalog(),
) : BaseGenerator(
    className = "BlockStateSpec",
    packageName = "block",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        writeGenerated(folder, "block_states", source(catalog.specs()))
    }

    private fun source(specs: List<BlockStateSpec>): String = buildString {
        appendLine("import '../../api/block_state_spec.dart';")
        appendLine("import '../../api/component_schema.dart';")
        appendLine()
        appendLine("/// The state properties of all blocks, sorted by key.")
        appendLine("const List<$className> blockStates = [")
        specs.forEach { appendLine("  ${it.toDart()},") }
        appendLine("];")
    }
}
