package net.theevilreaper.stelaris.cli.exporter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ExportStrategyTest {

    @ParameterizedTest
    @ValueSource(strings = ["local", "git"])
    fun `test from identifier`(identifier: String) {
        val strategy = ExportStrategy.fromIdentifier(identifier)
        assertNotNull(strategy)
        assertEquals(identifier, strategy?.identifier)
    }

    @Test
    fun `test from unknown identifier`() {
        assertNull(ExportStrategy.fromIdentifier("unknown"))
    }
}
