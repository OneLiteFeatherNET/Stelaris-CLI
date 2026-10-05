package net.theevilreaper.stelaris.cli.generator.dart.component

import net.minestom.server.color.DyeColor
import net.minestom.server.component.DataComponent
import net.minestom.server.entity.EquipmentSlot
import net.minestom.server.entity.EquipmentSlotGroup
import net.minestom.server.entity.attribute.AttributeOperation
import net.minestom.server.entity.metadata.animal.RabbitMeta
import net.minestom.server.entity.metadata.animal.tameable.ParrotMeta
import net.minestom.server.item.Material
import net.minestom.server.item.component.AttackRange
import net.minestom.server.item.component.BlocksAttacks
import net.minestom.server.item.component.Consumable
import net.minestom.server.item.component.CustomData
import net.minestom.server.item.component.CustomModelData
import net.minestom.server.item.component.DeathProtection
import net.minestom.server.item.component.EnchantmentList
import net.minestom.server.item.component.Equippable
import net.minestom.server.item.component.FireworkExplosion
import net.minestom.server.item.component.FireworkList
import net.minestom.server.item.component.Food
import net.minestom.server.item.component.ItemBlockState
import net.minestom.server.item.component.KineticWeapon
import net.minestom.server.item.component.PiercingWeapon
import net.minestom.server.item.component.SeededContainerLoot
import net.minestom.server.item.component.SwingAnimation
import net.minestom.server.item.component.Tool
import net.minestom.server.item.component.TooltipDisplay
import net.minestom.server.item.component.TypedCustomData
import net.minestom.server.item.component.UseCooldown
import net.minestom.server.item.component.UseEffects
import net.minestom.server.item.component.Weapon
import net.minestom.server.potion.PotionEffect
import net.minestom.server.potion.PotionType

/**
 * Hand maintained corrections for the reflection based schema derivation.
 *
 * Reflection only sees the Java model of Minestom, which is not always the vanilla format Stelaris has
 * to write. Everything that differs is corrected here. The [VANILLA] instance is the one the generator uses.
 * @property managed the keys of components which Stelaris handles with a dedicated editor
 * @property nonEditable the keys of components which hold runtime state and can't be set by a user
 * @property componentSchemas replaces the derived schema of a component, keyed by the component key
 * @property typeSchemas replaces the derived schema of a Java type wherever it occurs
 * @property fieldNames renames record fields whose snake case name is not the vanilla name
 * @property optionalFields record fields which can be left out in the vanilla format
 * @property enumValues renames enum constants whose lowercase name is not the vanilla name
 * @property registries registry names for types whose snake case name is not the registry name
 * @property displayNames replaces the derived display name of a component, keyed by the component key
 * @property categories the category of each component, keyed by the component key
 * @since 1.0.0
 */
class ComponentOverrides(
    val managed: Set<String> = emptySet(),
    val nonEditable: Set<String> = emptySet(),
    val componentSchemas: Map<String, (ComponentSchemaResolver) -> ComponentSchema> = emptyMap(),
    val typeSchemas: Map<Class<*>, ComponentSchema> = emptyMap(),
    val fieldNames: Map<Class<*>, Map<String, String>> = emptyMap(),
    val optionalFields: Map<Class<*>, Set<String>> = emptyMap(),
    val enumValues: Map<Class<*>, Map<String, String>> = emptyMap(),
    val registries: Map<Class<*>, String> = emptyMap(),
    val displayNames: Map<String, String> = emptyMap(),
    val categories: Map<String, ComponentCategory> = emptyMap(),
) {

    /**
     * Returns the category of a component. Entity variants are recognized by the path of their key,
     * e.g. `minecraft:cat/variant`.
     * @param key the key of the component
     * @return the category or [ComponentCategory.OTHER] if the component has none
     */
    fun categoryOf(key: String): ComponentCategory = categories[key]
        ?: if (key.substringAfter(':').contains('/')) ComponentCategory.ENTITY_VARIANT else ComponentCategory.OTHER

    companion object {

        private val COLOR = IntSchema(min = 0, max = 0xFFFFFF)

        /**
         * The corrections for the vanilla item component format.
         */
        val VANILLA: ComponentOverrides = ComponentOverrides(
            managed = setOf(
                "minecraft:lore",
                "minecraft:enchantments",
                "minecraft:custom_name",
                "minecraft:item_name",
                "minecraft:custom_model_data",
            ),
            nonEditable = setOf(
                "minecraft:bundle_contents",
                "minecraft:container",
                "minecraft:charged_projectiles",
                "minecraft:lodestone_tracker",
                "minecraft:debug_stick_state",
                "minecraft:map_id",
                "minecraft:map_decorations",
                "minecraft:map_post_processing",
                "minecraft:creative_slot_lock",
                "minecraft:intangible_projectile",
                "minecraft:bees",
                "minecraft:sulfur_cube_content",
            ),
            componentSchemas = mapOf(
                // Bounds
                "minecraft:max_stack_size" to { _ -> IntSchema(min = 1, max = 99) },
                "minecraft:max_damage" to { _ -> IntSchema(min = 1) },
                "minecraft:damage" to { _ -> IntSchema(min = 0) },
                "minecraft:repair_cost" to { _ -> IntSchema(min = 0) },
                "minecraft:ominous_bottle_amplifier" to { _ -> IntSchema(min = 0, max = 4) },
                "minecraft:minimum_attack_charge" to { _ -> FloatSchema(min = 0.0, max = 1.0) },
                "minecraft:potion_duration_scale" to { _ -> FloatSchema(min = 0.0) },
                "minecraft:lore" to { _ -> ListSchema(TextSchema, maxLength = 256) },
                // Minestom unwraps these values, the vanilla format has an object around them
                "minecraft:enchantable" to { _ -> ObjectSchema(mapOf("value" to ComponentField(IntSchema(min = 1)))) },
                "minecraft:repairable" to { _ ->
                    ObjectSchema(mapOf("items" to ComponentField(ListSchema(KeySchema("item")))))
                },
                // Minestom wraps these values, the vanilla format is the list itself
                "minecraft:banner_patterns" to { resolver ->
                    ListSchema(
                        ObjectSchema(
                            mapOf(
                                "pattern" to ComponentField(KeySchema("banner_pattern")),
                                "color" to ComponentField(resolver.schemaOf(DyeColor::class.java)),
                            )
                        )
                    )
                },
                "minecraft:suspicious_stew_effects" to { _ ->
                    ListSchema(
                        ObjectSchema(
                            mapOf(
                                "id" to ComponentField(KeySchema("mob_effect")),
                                "duration" to ComponentField(IntSchema(), optional = true),
                            )
                        )
                    )
                },
                "minecraft:pot_decorations" to { _ -> ListSchema(KeySchema("item"), maxLength = 4) },
                "minecraft:attribute_modifiers" to { resolver ->
                    ListSchema(
                        ObjectSchema(
                            mapOf(
                                "type" to ComponentField(KeySchema("attribute")),
                                "id" to ComponentField(KeySchema()),
                                "amount" to ComponentField(FloatSchema()),
                                "operation" to ComponentField(resolver.schemaOf(AttributeOperation::class.java)),
                                "slot" to ComponentField(
                                    resolver.schemaOf(EquipmentSlotGroup::class.java),
                                    optional = true
                                ),
                            )
                        )
                    )
                },
                // Block predicates, only the block list is offered
                "minecraft:can_place_on" to { _ -> blockPredicates() },
                "minecraft:can_break" to { _ -> blockPredicates() },
                // Item predicate, only the item list and the count are offered
                "minecraft:lock" to { _ ->
                    ObjectSchema(
                        mapOf(
                            "items" to ComponentField(ListSchema(KeySchema("item")), optional = true),
                            "count" to ComponentField(
                                ObjectSchema(
                                    mapOf(
                                        "min" to ComponentField(IntSchema(min = 1), optional = true),
                                        "max" to ComponentField(IntSchema(min = 1), optional = true),
                                    )
                                ),
                                optional = true
                            ),
                        )
                    )
                },
                // The effect settings are nested in Minestom and flat in the vanilla format
                "minecraft:potion_contents" to { _ ->
                    ObjectSchema(
                        mapOf(
                            "potion" to ComponentField(KeySchema("potion"), optional = true),
                            "custom_color" to ComponentField(COLOR, optional = true),
                            "custom_effects" to ComponentField(
                                ListSchema(
                                    ObjectSchema(
                                        mapOf(
                                            "id" to ComponentField(KeySchema("mob_effect")),
                                            "amplifier" to ComponentField(IntSchema(min = 0, max = 255), optional = true),
                                            "duration" to ComponentField(IntSchema(), optional = true),
                                            "ambient" to ComponentField(BoolSchema, optional = true),
                                            "show_particles" to ComponentField(BoolSchema, optional = true),
                                            "show_icon" to ComponentField(BoolSchema, optional = true),
                                        )
                                    )
                                ),
                                optional = true
                            ),
                            "custom_name" to ComponentField(StringSchema, optional = true),
                        )
                    )
                },
                // Book pages are filtered texts, which reflection can't resolve through the type variable
                "minecraft:writable_book_content" to { _ ->
                    ObjectSchema(
                        mapOf("pages" to ComponentField(ListSchema(filteredText(StringSchema), maxLength = 100), optional = true))
                    )
                },
                "minecraft:written_book_content" to { _ ->
                    ObjectSchema(
                        mapOf(
                            "title" to ComponentField(filteredText(StringSchema)),
                            "author" to ComponentField(StringSchema),
                            "generation" to ComponentField(IntSchema(min = 0, max = 3), optional = true),
                            "pages" to ComponentField(ListSchema(filteredText(TextSchema)), optional = true),
                            "resolved" to ComponentField(BoolSchema, optional = true),
                        )
                    )
                },
                // The profile is an either type in Minestom, only a profile by name is offered
                "minecraft:profile" to { _ -> ObjectSchema(mapOf("name" to ComponentField(StringSchema))) },
                // Strings in Minestom which are keys in the vanilla format
                "minecraft:item_model" to { _ -> KeySchema() },
                "minecraft:tooltip_style" to { _ -> KeySchema() },
                "minecraft:note_block_sound" to { _ -> KeySchema("sound_event") },
                "minecraft:recipes" to { _ -> ListSchema(KeySchema("recipe")) },
                // A single tag reference in the vanilla format
                "minecraft:provides_banner_patterns" to { _ -> KeySchema("banner_pattern") },
            ),
            typeSchemas = mapOf(
                // Arbitrary NBT and maps can't be described by the schema
                CustomData::class.java to UnsupportedSchema("CustomData"),
                TypedCustomData::class.java to UnsupportedSchema("TypedCustomData"),
                ItemBlockState::class.java to UnsupportedSchema("ItemBlockState"),
                EnchantmentList::class.java to UnsupportedSchema("EnchantmentList"),
            ),
            fieldNames = mapOf(
                Food::class.java to mapOf("saturationModifier" to "saturation"),
                Consumable::class.java to mapOf("effects" to "on_consume_effects"),
            ),
            optionalFields = mapOf(
                Food::class.java to setOf("canAlwaysEat"),
                Consumable::class.java to setOf("consumeSeconds", "animation", "sound", "hasConsumeParticles", "effects"),
                UseCooldown::class.java to setOf("cooldownGroup"),
                UseEffects::class.java to setOf("canSprint", "interactVibrations", "speedMultiplier"),
                Tool::class.java to setOf("defaultMiningSpeed", "damagePerBlock", "canDestroyBlocksInCreative"),
                Weapon::class.java to setOf("itemDamagePerAttack", "disableBlockingForSeconds"),
                AttackRange::class.java to setOf(
                    "minReach", "maxReach", "minCreativeReach", "maxCreativeReach", "hitboxMargin", "mobFactor"
                ),
                Equippable::class.java to setOf(
                    "equipSound", "assetId", "cameraOverlay", "allowedEntities", "dispensable", "swappable",
                    "damageOnHurt", "equipOnInteract", "canBeSheared", "shearingSound"
                ),
                BlocksAttacks::class.java to setOf(
                    "blockDelaySeconds", "disableCooldownScale", "damageReductions", "itemDamage", "bypassedBy",
                    "blockSound", "disableSound"
                ),
                BlocksAttacks.DamageReduction::class.java to setOf("horizontalBlockingAngle", "type"),
                KineticWeapon::class.java to setOf(
                    "contactCooldownTicks", "delayTicks", "dismountConditions", "knockbackConditions",
                    "damageConditions", "forwardMovement", "damageMultiplier", "sound", "hitSound"
                ),
                KineticWeapon.Condition::class.java to setOf("minSpeed", "minRelativeSpeed"),
                PiercingWeapon::class.java to setOf("dealsKnockback", "dismounts", "sound", "hitSound"),
                SwingAnimation::class.java to setOf("type", "duration"),
                DeathProtection::class.java to setOf("deathEffects"),
                TooltipDisplay::class.java to setOf("hideTooltip", "hiddenComponents"),
                CustomModelData::class.java to setOf("floats", "flags", "strings", "colors"),
                FireworkExplosion::class.java to setOf("colors", "fadeColors", "hasTrail", "hasTwinkle"),
                FireworkList::class.java to setOf("flightDuration", "explosions"),
                SeededContainerLoot::class.java to setOf("seed"),
            ),
            enumValues = mapOf(
                EquipmentSlot::class.java to EquipmentSlot.entries.associate { it.name to it.nbtName() },
                EquipmentSlotGroup::class.java to EquipmentSlotGroup.entries.associate { it.name to it.nbtName() },
                RabbitMeta.Variant::class.java to mapOf(
                    "BLACK_AND_WHITE" to "white_splotched",
                    "SALT_AND_PEPPER" to "salt",
                    "KILLER_BUNNY" to "evil",
                ),
                ParrotMeta.Color::class.java to mapOf("GREY" to "gray"),
            ),
            registries = mapOf(
                Material::class.java to "item",
                PotionType::class.java to "potion",
                PotionEffect::class.java to "mob_effect",
                DataComponent::class.java to "data_component_type",
            ),
            displayNames = mapOf(
                "minecraft:trim" to "Armor Trim",
                "minecraft:profile" to "Player Profile",
                "minecraft:enchantment_glint_override" to "Enchantment Glint",
            ),
            categories = categories(
                ComponentCategory.PROPERTIES to listOf(
                    "max_stack_size", "damage", "max_damage", "unbreakable", "repairable", "repair_cost",
                    "damage_resistant", "break_sound",
                ),
                ComponentCategory.DISPLAY to listOf(
                    "custom_name", "item_name", "lore", "item_model", "custom_model_data", "rarity", "tooltip_display",
                    "tooltip_style", "enchantment_glint_override", "dyed_color", "map_color", "profile", "trim",
                ),
                ComponentCategory.ENCHANTMENT to listOf("enchantments", "stored_enchantments", "enchantable"),
                ComponentCategory.COMBAT to listOf(
                    "attribute_modifiers", "weapon", "attack_range", "kinetic_weapon", "piercing_weapon",
                    "blocks_attacks", "swing_animation", "minimum_attack_charge", "damage_type", "death_protection",
                ),
                ComponentCategory.TOOL to listOf("tool", "can_break", "can_place_on"),
                ComponentCategory.EQUIPMENT to listOf("equippable", "glider"),
                ComponentCategory.CONSUMABLE to listOf(
                    "food", "consumable", "use_cooldown", "use_effects", "use_remainder", "potion_contents",
                    "potion_duration_scale", "suspicious_stew_effects", "ominous_bottle_amplifier",
                ),
                ComponentCategory.DECORATION to listOf(
                    "banner_patterns", "base_color", "pot_decorations", "provides_banner_patterns",
                    "provides_trim_material", "dye",
                ),
                ComponentCategory.SPECIAL to listOf(
                    "fireworks", "firework_explosion", "instrument", "jukebox_playable", "note_block_sound",
                    "additional_trade_cost", "recipes", "writable_book_content", "written_book_content",
                ),
                ComponentCategory.CONTENT to listOf(
                    "container", "container_loot", "bundle_contents", "charged_projectiles", "bees",
                    "sulfur_cube_content", "map_id", "map_decorations", "map_post_processing", "lodestone_tracker", "lock",
                ),
                ComponentCategory.DATA to listOf(
                    "custom_data", "block_entity_data", "block_state", "bucket_entity_data", "entity_data",
                    "debug_stick_state", "creative_slot_lock", "intangible_projectile",
                ),
            ),
        )

        /**
         * Maps every listed component to its category. The components are given without the namespace.
         */
        private fun categories(vararg entries: Pair<ComponentCategory, List<String>>): Map<String, ComponentCategory> =
            entries.flatMap { (category, paths) -> paths.map { "minecraft:$it" to category } }.toMap()

        private fun blockPredicates(): ComponentSchema =
            ListSchema(ObjectSchema(mapOf("blocks" to ComponentField(ListSchema(KeySchema("block"))))))

        private fun filteredText(text: ComponentSchema): ComponentSchema = ObjectSchema(
            mapOf(
                "raw" to ComponentField(text),
                "filtered" to ComponentField(text, optional = true),
            )
        )
    }
}
