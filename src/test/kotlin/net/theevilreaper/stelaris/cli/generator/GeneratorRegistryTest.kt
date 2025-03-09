package net.theevilreaper.stelaris.cli.generator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GeneratorRegistryTest {

    @Test
    fun testGetGenerators() {
        val generatorRegistry = GeneratorRegistry()
        val generators = generatorRegistry.getGenerators()
        assertEquals(7, generators.size)
    }

    @Test
    fun testGetGeneratorsWithPredicate() {
        val generatorRegistry = GeneratorRegistry()
        val generators = generatorRegistry.getGenerators { it.isExperimental() }
        assertFalse(generators.isEmpty())
        assertEquals(1, generators.size)
    }
}