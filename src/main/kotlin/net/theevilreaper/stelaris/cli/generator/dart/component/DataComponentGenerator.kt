package net.theevilreaper.stelaris.cli.generator.dart.component

import com.google.auto.service.AutoService
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Path

/**
 * Generates the catalog of all data components together with the schema classes it is built from.
 *
 * The files are written as templates instead of through DartPoet, because the catalog is a const list
 * of nested constructor calls.
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
        writeGenerated(folder, SCHEMA_FILE, schemaTemplate())
        writeGenerated(folder, CATALOG_FILE, catalogSource(catalog.specs()))
    }

    private fun schemaTemplate(): String {
        val stream = checkNotNull(javaClass.getResourceAsStream(SCHEMA_TEMPLATE)) { "Missing template $SCHEMA_TEMPLATE" }
        return stream.use { it.readBytes().toString(Charsets.UTF_8) }
    }

    private fun catalogSource(specs: List<ComponentSpec>): String = buildString {
        appendLine("import '$SCHEMA_FILE.dart';")
        appendLine()
        appendLine("/// All data components which can be set on an item, sorted by key.")
        appendLine("const List<$className> dataComponents = [")
        specs.forEach { appendLine("  ${it.toDart()},") }
        appendLine("];")
    }

    private companion object {
        private const val SCHEMA_FILE = "component_schema"
        private const val CATALOG_FILE = "data_components"
        private const val SCHEMA_TEMPLATE = "/dart/$SCHEMA_FILE.dart"
    }
}
