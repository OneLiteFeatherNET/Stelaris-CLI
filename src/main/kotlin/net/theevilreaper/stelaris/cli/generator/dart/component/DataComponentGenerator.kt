package net.theevilreaper.stelaris.cli.generator.dart.component

import com.google.auto.service.AutoService
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Path

/**
 * Generates the catalog of all data components.
 *
 * The schema classes and the [ComponentCategory] enum live in `lib/src/api` of the data repository,
 * only the catalog is generated. It is written as plain source instead of through DartPoet, because
 * it is a const list of nested constructor calls.
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "DataComponentGenerator")
class DataComponentGenerator(
    private val catalog: DataComponentCatalog = DataComponentCatalog(),
) : BaseGenerator(
    className = "ComponentSpec",
    packageName = "component",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        writeGenerated(folder, "data_components", source(catalog.specs()))
    }

    private fun source(specs: List<ComponentSpec>): String = buildString {
        appendLine("import '../../api/component_category.dart';")
        appendLine("import '../../api/component_schema.dart';")
        appendLine()
        appendLine("/// All data components which can be set on an item, sorted by key.")
        appendLine("const List<$className> dataComponents = [")
        specs.forEach { appendLine("  ${it.toDart()},") }
        appendLine("];")
    }
}
