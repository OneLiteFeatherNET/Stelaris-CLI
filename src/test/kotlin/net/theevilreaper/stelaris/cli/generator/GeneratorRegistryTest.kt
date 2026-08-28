package net.theevilreaper.stelaris.cli.generator

import jakarta.inject.Provider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Path

class GeneratorRegistryTest {

    private class NoopGenerator : Generator {
        override fun generate(outputPath: Path) = Unit
        override fun cleanUp() = Unit
    }

    private fun descriptor(
        name: String,
        experimental: Boolean = false,
        onCreate: () -> Unit = {},
    ) = GeneratorDescriptor(name, experimental, Provider { onCreate(); NoopGenerator() })

    @Test
    fun `test registry rejects an empty descriptor set`() {
        val exception = assertThrows<IllegalStateException> { GeneratorRegistry(emptySet()) }
        assertTrue(
            exception.message!!.contains("mergeServiceFiles()"),
            "The message should point at the most likely cause",
        )
    }

    @Test
    fun `test registry rejects duplicated generator names`() {
        val exception = assertThrows<IllegalStateException> {
            GeneratorRegistry(setOf(descriptor("SoundEventGenerator"), descriptor("SoundEventGenerator")))
        }
        assertTrue(exception.message!!.contains("SoundEventGenerator"), "The message should name the duplicate")
    }

    @Test
    fun `test all descriptors are returned`() {
        val registry = GeneratorRegistry(setOf(descriptor("First"), descriptor("Second")))
        assertEquals(setOf("First", "Second"), registry.getDescriptors().map { it.name }.toSet())
    }

    @Test
    fun `test filtered out generators are never created`() {
        var experimentalCreated = false
        val registry = GeneratorRegistry(
            setOf(
                descriptor("Stable"),
                descriptor("Experimental", experimental = true) { experimentalCreated = true },
            ),
        )

        val generators = registry.createGenerators { !it.experimental }

        assertEquals(1, generators.size, "Only the stable generator should be created")
        assertFalse(experimentalCreated, "An excluded generator must not be instantiated")
    }

    @Test
    fun `test createGenerators without a filter creates every generator`() {
        val registry = GeneratorRegistry(setOf(descriptor("First"), descriptor("Second")))
        assertEquals(2, registry.createGenerators().size)
    }
}
