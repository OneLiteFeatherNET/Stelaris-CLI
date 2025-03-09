package net.theevilreaper.stelaris.cli.exporter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class LocalProjectExporterTest {

    @Test
    fun `test invalid version usage`() {
        assertThrowsExactly(
            IllegalArgumentException::class.java,
            { LocalProjectExporter(Paths.get(""), "", setOf()) },
            "The version must not be null or empty"
        )
    }
}
