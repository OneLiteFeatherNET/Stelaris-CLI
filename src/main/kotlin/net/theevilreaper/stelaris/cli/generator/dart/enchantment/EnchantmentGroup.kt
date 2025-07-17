package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import net.theevilreaper.stelaris.cli.util.MinecraftHelper

enum class EnchantmentGroup(val classPart: String, val keywords: Set<String>) {
    ARMOR("armor", setOf("armor", "foot_armor", "head_armor", "leg_armor", "equippable")),
    WEAPON("weapon", setOf("sword", "bow", "trident", "mace", "weapon", "fire_aspect", "sharp_weapon", "crossbow")),
    TOOL("tool", setOf("mining", "mining_loot", "fishing")),
    MISC("meta", setOf("vanishing", "durability"))
    ;

    companion object {

        /**
         * Matches the given group string to an [EnchantmentGroup].
         * If the group is empty or not recognized, it returns null.
         *
         * @param group the group string to match
         * @return the matched [EnchantmentGroup]
         */
        @JvmStatic
        fun matchGroup(group: String): EnchantmentGroup? {
            if (group.trim().isEmpty()) return null

            val extractedGroup = MinecraftHelper.extractEnchantmentGroup(group)

            if (extractedGroup == null) {
                return null
            }
            return entries.find { it.keywords.contains(extractedGroup) }
        }
    }

}