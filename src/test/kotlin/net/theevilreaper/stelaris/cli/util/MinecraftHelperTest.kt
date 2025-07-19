package net.theevilreaper.stelaris.cli.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MinecraftHelperTest {

    @Test
    fun `test group parsing`() {
        val group = "minecraft:enchantable/sword"
        val parsedGroup = MinecraftHelper.extractEnchantmentGroup(group)
        assertNotNull(parsedGroup, "Parsed group should not be null")
        assertEquals("sword", parsedGroup, "Parsed group does not match expected value")
    }
}
