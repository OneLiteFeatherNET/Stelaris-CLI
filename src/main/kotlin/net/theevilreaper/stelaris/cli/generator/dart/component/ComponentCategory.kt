package net.theevilreaper.stelaris.cli.generator.dart.component

import net.theevilreaper.stelaris.cli.util.StringHelper

/**
 * Groups the data components, so a user interface can offer them in sections instead of one long list.
 *
 * The entries have to stay in sync with `lib/src/api/component_category.dart` of the data repository,
 * which also holds the display names and the order of the sections. The Dart enum has one more entry,
 * `custom`, for components an application adds itself; the generator never assigns it.
 * @since 1.0.0
 */
enum class ComponentCategory {
    PROPERTIES,
    DISPLAY,
    ENCHANTMENT,
    COMBAT,
    TOOL,
    EQUIPMENT,
    CONSUMABLE,
    DECORATION,
    SPECIAL,
    CONTENT,
    ENTITY_VARIANT,
    DATA,

    /**
     * The fallback for components which have no category yet, e.g. after a Minecraft update.
     */
    OTHER;

    /**
     * The name of the entry in the Dart enum, e.g. `entityVariant`.
     */
    val dartName: String = StringHelper.toLowerCamelCase(name)
}
