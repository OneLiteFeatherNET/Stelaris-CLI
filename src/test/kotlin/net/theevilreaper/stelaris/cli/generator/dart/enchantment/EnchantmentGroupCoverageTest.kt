package net.theevilreaper.stelaris.cli.generator.dart.enchantment

import net.minestom.server.MinecraftServer
import net.minestom.testing.Env
import net.minestom.testing.extension.MicrotusExtension
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MicrotusExtension::class)
class EnchantmentGroupCoverageTest {

    @Test
    fun `test every enchantment belongs to a group`(env: Env) {
        val registry = MinecraftServer.getEnchantmentRegistry()
        val ungrouped = registry.keys().filter { key ->
            val tag = registry.get(key)?.supportedItems()?.key()?.key()?.asString() ?: ""
            EnchantmentGroup.matchGroup(tag) == null
        }.map { it.key().asString() }
        assertTrue(ungrouped.isEmpty(), "Enchantments without a group: $ungrouped")
    }
}
