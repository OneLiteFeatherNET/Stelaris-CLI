package net.theevilreaper.stelaris.cli.generator.dart.component

import net.theevilreaper.stelaris.cli.util.StringHelper

/**
 * Groups the data components, so a user interface can offer them in sections instead of one long list.
 *
 * The order of the entries is the order in which the sections should be shown.
 * @property displayName the name of the section which can be shown in a user interface
 * @since 1.0.0
 */
enum class ComponentCategory(val displayName: String) {
    PROPERTIES("Properties"),
    DISPLAY("Display"),
    ENCHANTMENT("Enchantment"),
    COMBAT("Combat"),
    TOOL("Tool"),
    EQUIPMENT("Equipment"),
    CONSUMABLE("Consumable"),
    DECORATION("Decoration"),
    SPECIAL("Special"),
    CONTENT("Content"),
    ENTITY_VARIANT("Entity Variant"),
    DATA("Data"),

    /**
     * The fallback for components which have no category yet, e.g. after a Minecraft update.
     */
    OTHER("Other");

    /**
     * The key of the category, e.g. `entity_variant`.
     */
    val key: String = name.lowercase()

    /**
     * The name of the entry in the generated Dart enum, e.g. `entityVariant`.
     */
    val dartName: String = StringHelper.toLowerCamelCase(name)
}
