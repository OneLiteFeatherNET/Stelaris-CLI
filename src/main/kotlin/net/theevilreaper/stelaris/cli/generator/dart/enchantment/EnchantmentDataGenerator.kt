package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import com.google.auto.service.AutoService
import net.minestom.server.MinecraftServer
import net.minestom.server.item.enchant.Enchantment
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DartSource
import java.nio.file.Path

/**
 * Generates the data of every enchantment: which items support it and which enchantments it excludes.
 *
 * With this table the UI can offer exactly the enchantments that fit a material and reject incompatible
 * combinations, without maintaining its own lists.
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "EnchantmentDataGenerator")
class EnchantmentDataGenerator : BaseGenerator(
    className = "EnchantmentData",
    packageName = "enchantment",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val registry = MinecraftServer.getEnchantmentRegistry()
        val enchantments = registry.keys()
            .map { it.key().asString() to requireNotNull(registry.get(it)) }
            .sortedBy { it.first }
        DartSource.write(folder, "enchantment_data", source(enchantments))
    }

    private fun source(enchantments: List<Pair<String, Enchantment>>): String = buildString {
        appendLine(DartSource.GENERATED_HEADER)
        appendLine()
        appendLine("/// The data of an enchantment.")
        appendLine("final class $className {")
        appendLine("  /// The key of the enchantment, e.g. `minecraft:sharpness`.")
        appendLine("  final String key;")
        appendLine("  final int maxLevel;")
        appendLine()
        appendLine("  /// The keys of the materials the enchantment can be applied to.")
        appendLine("  final Set<String> supportedItems;")
        appendLine()
        appendLine("  /// The keys of the enchantments which can't be combined with this one.")
        appendLine("  final Set<String> exclusiveWith;")
        appendLine()
        appendLine("  /// The equipment slots in which the enchantment takes effect.")
        appendLine("  final List<String> slots;")
        appendLine()
        appendLine("  const $className(this.key, {required this.maxLevel, required this.supportedItems, required this.exclusiveWith, required this.slots});")
        appendLine("}")
        appendLine()
        appendLine("/// The data of every enchantment, keyed by the enchantment key.")
        appendLine("const Map<String, $className> enchantmentData = {")
        enchantments.forEach { (key, enchantment) -> appendLine("  ${DartSource.string(key)}: ${data(key, enchantment)},") }
        appendLine("};")
    }

    private fun data(key: String, enchantment: Enchantment): String {
        val supportedItems = enchantment.supportedItems().map { it.key().asString() }.sorted()
        val exclusiveWith = enchantment.exclusiveSet().map { it.key().asString() }.filter { it != key }.sorted()
        val slots = enchantment.slots().map { it.nbtName() }
        return "$className(${DartSource.string(key)}, " +
            "maxLevel: ${enchantment.maxLevel()}, " +
            "supportedItems: {${supportedItems.joinToString(", ", transform = DartSource::string)}}, " +
            "exclusiveWith: {${exclusiveWith.joinToString(", ", transform = DartSource::string)}}, " +
            "slots: [${slots.joinToString(", ", transform = DartSource::string)}])"
    }
}
