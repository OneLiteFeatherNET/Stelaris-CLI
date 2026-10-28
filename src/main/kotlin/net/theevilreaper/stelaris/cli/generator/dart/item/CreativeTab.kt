package net.theevilreaper.stelaris.cli.generator.dart.item

/**
 * The item groups of the creative inventory.
 *
 * Minestom does not ship the creative mode tab registry, so the entries mirror the keys of
 * `net.minecraft.world.item.CreativeModeTabs`. The special tabs (search, hotbar, inventory)
 * are left out because they don't group any items.
 */
enum class CreativeTab(val key: String, val displayName: String) {
    BUILDING_BLOCKS("building_blocks", "Building Blocks"),
    COLORED_BLOCKS("colored_blocks", "Colored Blocks"),
    NATURAL_BLOCKS("natural_blocks", "Natural Blocks"),
    FUNCTIONAL_BLOCKS("functional_blocks", "Functional Blocks"),
    REDSTONE_BLOCKS("redstone_blocks", "Redstone Blocks"),
    TOOLS_AND_UTILITIES("tools_and_utilities", "Tools & Utilities"),
    COMBAT("combat", "Combat"),
    FOOD_AND_DRINKS("food_and_drinks", "Food & Drinks"),
    INGREDIENTS("ingredients", "Ingredients"),
    SPAWN_EGGS("spawn_eggs", "Spawn Eggs"),
    OP_BLOCKS("op_blocks", "Operator Utilities"),
}
