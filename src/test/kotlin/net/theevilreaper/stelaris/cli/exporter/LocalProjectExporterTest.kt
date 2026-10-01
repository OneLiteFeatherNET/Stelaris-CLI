package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.stelaris.cli.generator.Generator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class LocalProjectExporterTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `test export creates generated directory and invokes generators`() {
        var generatedTarget: Path? = null
        val mockGenerator = object : Generator {
            override fun generate(outputPath: Path) {
                generatedTarget = outputPath
            }
            override fun cleanUp() {}
        }

        val exportPath = tempDir.resolve("export_target")
        val exporter = LocalProjectExporter(exportPath, setOf(mockGenerator))
        exporter.export()

        assertTrue(Files.exists(exportPath), "Export path should be created")
        val generatedPath = exportPath.resolve("lib").resolve("src").resolve("generated")
        assertTrue(Files.exists(generatedPath), "generated path should be created")
        assertTrue(generatedTarget == generatedPath, "Generator should be called with the generated path")
    }
}
