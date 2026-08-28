package net.theevilreaper.stelaris.cli.generator.dart.entity

import net.minestom.server.entity.EntityType
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING

/**
 * Represents the different entity categories in Minecraft.
 * @param type the type name of the category
 * @param entityNames the set of entity names belonging to this category
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
enum class EntitySubType(val type: String, val entityNames: Set<String>) {

    ANIMAL(
        "animal",
        setOf(
            "allay", "armadillo", "bee", "camel", "camel_husk", "cat", "chicken",
            "copper_golem", "cow", "donkey", "fox", "frog", "goat", "happy_ghast",
            "horse", "llama", "mooshroom", "mule", "ocelot", "panda", "parched",
            "parrot", "pig", "polar_bear", "rabbit", "sheep", "skeleton_horse",
            "sniffer", "snow_golem", "strider", "trader_llama", "villager",
            "wandering_trader", "wolf", "zombie_horse"
        )
    ),
    MONSTER(
        "monster",
        setOf(
            "blaze", "bogged", "breeze", "cave_spider", "creaking", "creeper",
            "drowned", "elder_guardian", "ender_dragon", "enderman", "endermite",
            "evoker", "ghast", "giant", "guardian", "hoglin", "husk", "illusioner",
            "iron_golem", "magma_cube", "phantom", "piglin", "piglin_brute",
            "pillager", "ravager", "shulker", "silverfish", "skeleton", "slime",
            "spider", "stray", "sulfur_cube", "vex", "vindicator", "warden",
            "witch", "wither", "wither_skeleton", "zoglin", "zombie",
            "zombie_villager", "zombified_piglin"
        )
    ),
    WATER(
        "water",
        setOf(
            "axolotl", "bat", "cod", "dolphin", "glow_squid", "nautilus",
            "pufferfish", "salmon", "squid", "tadpole", "tropical_fish",
            "turtle", "zombie_nautilus"
        )
    ),
    PROJECTILE(
        "projectile",
        setOf(
            "arrow", "spectral_arrow", "trident", "snowball", "egg", "ender_pearl",
            "experience_bottle", "splash_potion", "lingering_potion", "eye_of_ender",
            "fireball", "small_fireball", "dragon_fireball", "wither_skull",
            "shulker_bullet", "llama_spit", "wind_charge", "breeze_wind_charge",
            "fishing_bobber", "firework_rocket"
        )
    ),
    VEHICLE(
        "vehicle",
        setOf(
            "acacia_boat", "acacia_chest_boat", "bamboo_chest_raft", "bamboo_raft",
            "birch_boat", "birch_chest_boat", "cherry_boat", "cherry_chest_boat",
            "dark_oak_boat", "dark_oak_chest_boat", "jungle_boat", "jungle_chest_boat",
            "mangrove_boat", "mangrove_chest_boat", "oak_boat", "oak_chest_boat",
            "pale_oak_boat", "pale_oak_chest_boat", "spruce_boat", "spruce_chest_boat",
            "minecart", "chest_minecart", "command_block_minecart", "furnace_minecart",
            "hopper_minecart", "spawner_minecart", "tnt_minecart"
        )
    ),
    DISPLAY(
        "display",
        setOf(
            "item_frame", "glow_item_frame", "painting", "armor_stand",
            "item_display", "block_display", "text_display", "interaction",
            "marker", "mannequin", "area_effect_cloud", "end_crystal",
            "experience_orb", "falling_block", "item", "leash_knot",
            "lightning_bolt", "ominous_item_spawner", "player", "tnt", "evoker_fangs"
        )
    );

    fun matches(entityType: EntityType): Boolean {
        val rawName = entityType.name().replace("minecraft:", EMPTY_STRING)
        return entityNames.contains(rawName)
    }
}
