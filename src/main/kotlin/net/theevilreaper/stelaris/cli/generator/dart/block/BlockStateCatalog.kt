package net.theevilreaper.stelaris.cli.generator.dart.block

import net.minestom.server.instance.block.Block
import net.theevilreaper.stelaris.cli.generator.dart.component.BoolSchema
import net.theevilreaper.stelaris.cli.generator.dart.component.ComponentSchema
import net.theevilreaper.stelaris.cli.generator.dart.component.EnumSchema
import net.theevilreaper.stelaris.cli.generator.dart.component.IntSchema
import net.theevilreaper.stelaris.cli.generator.dart.component.displayName
import net.theevilreaper.stelaris.cli.generator.dart.util.DartSource

/**
 * Describes the state properties of a single block, e.g. the keys which can be used in a blockstate
 * file of a resource pack.
 * @property key the key of the block, e.g. `minecraft:pink_petals`
 * @property displayName the name of the block which can be shown in a user interface
 * @property properties the schema of every state property, sorted by name
 * @property defaults the value of every state property in the default state of the block
 */
data class BlockStateSpec(
    val key: String,
    val displayName: String,
    val properties: Map<String, ComponentSchema>,
    val defaults: Map<String, String>,
) {
    fun toDart(): String {
        val properties = properties.entries.joinToString(", ") { (name, schema) ->
            "${DartSource.string(name)}: ${schema.toDart()}"
        }
        val defaults = defaults.entries.joinToString(", ") { (name, value) ->
            "${DartSource.string(name)}: ${DartSource.string(value)}"
        }
        return "BlockStateSpec(${DartSource.string(key)}, ${DartSource.string(displayName)}, {$properties}, {$defaults})"
    }
}

/**
 * Builds the [BlockStateSpec] of every block which Minestom knows.
 *
 * The property schemas reuse the component schemas: a property with the values `true` and `false` becomes a
 * [BoolSchema], a property with a contiguous range of numbers an [IntSchema] and everything else an [EnumSchema].
 * @since 1.0.0
 */
class BlockStateCatalog {

    /**
     * Derives a spec for each block from its possible states.
     * @return the specs, sorted by key
     */
    fun specs(): List<BlockStateSpec> = Block.values()
        .map { block ->
            val key = block.key().asString()
            BlockStateSpec(
                key = key,
                displayName = displayName(key),
                properties = propertyValues(block).mapValues { (_, values) -> schemaOf(values) }.toSortedMap(),
                defaults = block.defaultState().properties().toSortedMap(),
            )
        }
        .sortedBy { it.key }

    /**
     * Collects the values of every property over all possible states of the [block], in the order in which
     * they occur.
     */
    private fun propertyValues(block: Block): Map<String, List<String>> {
        val values = linkedMapOf<String, LinkedHashSet<String>>()
        block.possibleStates().forEach { state ->
            state.properties().forEach { (name, value) -> values.getOrPut(name, ::LinkedHashSet).add(value) }
        }
        return values.mapValues { it.value.toList() }
    }

    internal fun schemaOf(values: List<String>): ComponentSchema {
        if (values.toSet() == setOf("true", "false")) return BoolSchema
        val numbers = values.map { it.toIntOrNull() ?: return EnumSchema(values) }.sorted()
        val contiguous = numbers.zipWithNext().all { (previous, next) -> next == previous + 1 }
        return if (contiguous) IntSchema(min = numbers.first(), max = numbers.last()) else EnumSchema(values)
    }
}
