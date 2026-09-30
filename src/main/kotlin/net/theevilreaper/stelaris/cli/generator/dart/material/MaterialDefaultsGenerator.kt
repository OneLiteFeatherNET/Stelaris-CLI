package net.theevilreaper.stelaris.cli.generator.dart.material

import com.google.auto.service.AutoService
import net.minestom.server.component.DataComponents
import net.minestom.server.item.Material
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DartSource
import java.nio.file.Path

/**
 * Generates the default component values of every material, taken from its prototype.
 *
 * Unlike the category enums of the [net.theevilreaper.stelaris.cli.generator.dart.MaterialGenerator]
 * the table covers all materials without a filter.
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "MaterialDefaultsGenerator")
class MaterialDefaultsGenerator : BaseGenerator(
    className = "MaterialDefaults",
    packageName = "materials",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val materials = Material.values().sortedBy { it.name() }
        DartSource.write(folder, "material_defaults", source(materials))
    }

    private fun source(materials: List<Material>): String = buildString {
        appendLine(DartSource.GENERATED_HEADER)
        appendLine()
        appendLine("/// The default component values of a material.")
        appendLine("final class $className {")
        appendLine("  final int maxStackSize;")
        appendLine("  final int? maxDamage;")
        appendLine("  final String? rarity;")
        appendLine()
        appendLine("  const $className({required this.maxStackSize, this.maxDamage, this.rarity});")
        appendLine("}")
        appendLine()
        appendLine("/// The default component values of every material, keyed by the material key.")
        appendLine("const Map<String, $className> materialDefaults = {")
        materials.forEach { appendLine("  ${DartSource.string(it.name())}: ${defaults(it)},") }
        appendLine("};")
    }

    private fun defaults(material: Material): String {
        val prototype = material.prototype()
        val arguments = buildList {
            add("maxStackSize: ${prototype.get(DataComponents.MAX_STACK_SIZE) ?: 1}")
            prototype.get(DataComponents.MAX_DAMAGE)?.let { add("maxDamage: $it") }
            prototype.get(DataComponents.RARITY)?.let { add("rarity: ${DartSource.string(it.name.lowercase())}") }
        }
        return "$className(${arguments.joinToString(", ")})"
    }
}
