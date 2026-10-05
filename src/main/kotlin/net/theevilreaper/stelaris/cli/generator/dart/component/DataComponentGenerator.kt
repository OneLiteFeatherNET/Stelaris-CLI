package net.theevilreaper.stelaris.cli.generator.dart.component

import com.google.auto.service.AutoService
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartFileBuilder
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.generator.dart.util.keyed
import net.theevilreaper.stelaris.cli.generator.dart.util.keyedLookup
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Path

/**
 * Generates the catalog of all data components together with the schema classes it is built from.
 *
 * The schema and the catalog are written as templates instead of through DartPoet, because the catalog
 * is a const list of nested constructor calls. Only the [ComponentCategory] enum goes through DartPoet,
 * so it follows the same format as every other generated enum.
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
        categoryFile().writeGenerated(folder, baseDir = outputPath.parent)
        writeGenerated(folder, SCHEMA_FILE, schemaTemplate())
        writeGenerated(folder, CATALOG_FILE, catalogSource(catalog.specs()))
    }

    private fun categoryFile(): DartFileBuilder {
        val entries = ComponentCategory.entries.map {
            EnumEntrySpec.builder(it.dartName)
                .parameter { EnumParameterSpec.positional("%C", it.displayName) }
                .parameter { EnumParameterSpec.positional("%C", it.key) }
                .build()
        }
        val enumClass = ClassSpec.enumClass(CATEGORY_CLASS)
            .keyed(CATEGORY_CLASS)
            .enumProperties(*entries.toTypedArray())
            .properties(*DEFAULT_PROPERTIES)
            .constructor {
                ConstructorSpec.builder(CATEGORY_CLASS)
                    .modifier { DartModifier.CONST }
                    .parameters(*DEFAULT_PARAMETERS)
                    .build()
            }
            .build()
        return DartFile.builder(CATEGORY_FILE)
            .keyedLookup(CATEGORY_CLASS)
            .type(enumClass)
    }

    private fun schemaTemplate(): String {
        val stream = checkNotNull(javaClass.getResourceAsStream(SCHEMA_TEMPLATE)) { "Missing template $SCHEMA_TEMPLATE" }
        return stream.use { it.readBytes().toString(Charsets.UTF_8) }
    }

    private fun catalogSource(specs: List<ComponentSpec>): String = buildString {
        appendLine("import '$CATEGORY_FILE.dart';")
        appendLine("import '$SCHEMA_FILE.dart';")
        appendLine()
        appendLine("/// All data components which can be set on an item, sorted by key.")
        appendLine("const List<$className> dataComponents = [")
        specs.forEach { appendLine("  ${it.toDart()},") }
        appendLine("];")
    }

    private companion object {
        private const val CATEGORY_CLASS = "ComponentCategory"
        private const val CATEGORY_FILE = "component_category"
        private const val SCHEMA_FILE = "component_schema"
        private const val CATALOG_FILE = "data_components"
        private const val SCHEMA_TEMPLATE = "/dart/$SCHEMA_FILE.dart"
    }
}
