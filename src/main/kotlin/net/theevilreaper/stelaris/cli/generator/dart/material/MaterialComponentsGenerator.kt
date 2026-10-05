package net.theevilreaper.stelaris.cli.generator.dart.material

import com.google.auto.service.AutoService
import net.minestom.server.item.Material
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DartSource
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Path

/**
 * Generates which data components every material has by default, taken from its prototype.
 *
 * Only the keys are generated, not the values. A user interface can use them to show which components
 * an item already brings along. The components every material has are written once, so the table only
 * contains what is special about a material.
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "MaterialComponentsGenerator")
class MaterialComponentsGenerator : BaseGenerator(
    className = "materialComponents",
    packageName = "material",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val components = Material.values()
            .sortedBy { it.name() }
            .associate { material ->
                material.name() to material.prototype().entrySet().map { it.component().key().asString() }.toSet()
            }
        writeGenerated(folder, "material_components", source(components))
    }

    private fun source(components: Map<String, Set<String>>): String = buildString {
        val base = components.values.reduce { shared, keys -> shared intersect keys }
        appendLine("/// The keys of the data components which every material has by default.")
        appendLine("const List<String> baseMaterialComponents = [${keyList(base)}];")
        appendLine()
        appendLine("/// The keys of the additional data components a material has by default, keyed by the material key.")
        appendLine("/// Materials which only have the [baseMaterialComponents] are left out.")
        appendLine("const Map<String, List<String>> $className = {")
        components.forEach { (material, keys) ->
            val additional = keys - base
            if (additional.isNotEmpty()) appendLine("  ${DartSource.string(material)}: [${keyList(additional)}],")
        }
        appendLine("};")
        appendLine()
        appendLine("/// Returns the keys of all data components the material with the given [key] has by default.")
        appendLine("List<String> defaultComponentsOf(String key) => [...baseMaterialComponents, ...?$className[key]];")
    }

    private fun keyList(keys: Set<String>): String = keys.sorted().joinToString(", ", transform = DartSource::string)
}
