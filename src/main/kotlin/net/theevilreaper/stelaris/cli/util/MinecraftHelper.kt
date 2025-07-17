package net.theevilreaper.stelaris.cli.util


object MinecraftHelper {

    private val minecraftPattern: Regex = Regex("^minecraft:enchantable/")

    fun extractEnchantmentGroup(group: String): String? {
        return if (!group.startsWith("minecraft:enchantable/")) {
            null
        } else {
            minecraftPattern.replace(group, "")
        }
    }
}