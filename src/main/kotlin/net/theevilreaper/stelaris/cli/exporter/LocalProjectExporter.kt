package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.stelaris.cli.generator.Generator
import java.nio.file.Files
import java.nio.file.Path

class LocalProjectExporter(
    private val exportPath: Path,
    private val generators: Set<Generator>
) : BaseExporter() {

    override fun export() {
        if (!Files.exists(exportPath)) {
            Files.createDirectories(exportPath)
        }
        val libPath: Path = exportPath.resolve("lib")
        if (!Files.exists(libPath)) Files.createDirectories(libPath)
        generators.forEach { generator -> generator.generate(libPath) }
    }
}