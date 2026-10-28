package net.theevilreaper.stelaris.cli.generator.dart.item

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CreativeTabTest {

    @Test
    fun `test every key matches the entry name`() {
        val mismatches = CreativeTab.entries.filter { it.key != it.name.lowercase() }
        assertTrue(mismatches.isEmpty(), "Creative tabs with a key that differs from their name: $mismatches")
    }

    @Test
    fun `test display names are unique and not blank`() {
        val displayNames = CreativeTab.entries.map { it.displayName }
        assertTrue(displayNames.none { it.isBlank() }, "Creative tabs with a blank display name")
        assertEquals(displayNames.size, displayNames.toSet().size, "Duplicate display names: $displayNames")
    }
}
