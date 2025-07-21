package net.theevilreaper.stelaris.cli.generator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class GeneratorRegistryTest {

    @Disabled("Need a rework")
    @Test
    fun testGetGenerators() {
        val generatorRegistry = GeneratorRegistry()
        val generators = generatorRegistry.getGenerators()
        assertEquals(8, generators.size)
    }
}
