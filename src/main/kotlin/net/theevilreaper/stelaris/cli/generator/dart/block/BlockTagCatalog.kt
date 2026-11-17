package net.theevilreaper.stelaris.cli.generator.dart.block

import net.minestom.server.instance.block.Block

/**
 * Collects the block tags of the game, e.g. `minecraft:stairs`, with the blocks each of them contains.
 *
 * Minestom resolves tags which reference other tags already, so every tag holds the keys of its blocks
 * directly. `minecraft:logs` for example contains the oak log itself and not the `minecraft:oak_logs` tag.
 * @since 1.0.0
 */
class BlockTagCatalog {

    /**
     * Collects every block tag with the keys of its blocks.
     * @return the keys of the blocks per tag key, the tags and the blocks of each tag sorted by key
     */
    fun tags(): Map<String, List<String>> = Block.staticRegistry().tags()
        .associate { tag -> tag.key()!!.key().asString() to tag.map { it.key().asString() }.sorted() }
        .toSortedMap()
}
