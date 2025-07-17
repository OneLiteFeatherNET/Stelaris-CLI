package net.theevilreaper.stelaris.cli.generator.enchantment

import net.theevilreaper.stelaris.cli.generator.dart.enchantment.EnchantmentGroup
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class nchantmentGroupTest {

    @Test
    fun `test group count should be 18`() {
        val groupCount = EnchantmentGroup.entries.sumOf { it.keywords.size }
        Assertions.assertEquals(18, groupCount, "Expected 18 enchantment groups, but found $groupCount")
    }

    @ParameterizedTest
    @ValueSource(strings = ["minecraft:enchantable/bow", "minecraft:enchantable/trident", "minecraft:enchantable/sword"])
    fun `test extractEnchantmentGroup with valid groups`(group: String) {
        val extractedGroup = EnchantmentGroup.Companion.matchGroup(group)
        Assertions.assertNotNull(extractedGroup, "Expected non-null group for $group")
        Assertions.assertEquals(EnchantmentGroup.WEAPON, extractedGroup)
    }
}