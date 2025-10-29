package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.stelaris.cli.generator.Generator
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class LocalProjectExporter(
    private val exportPath: Path,
    private val versionString: String,
    private val generators: Set<Generator>
) : BaseExporter() {

    init {
        require((versionString.isNotEmpty())) { "The version string can't be empty" }
    }

    override fun export() {
        println("Exporting to local path: $exportPath")

        if (!Files.exists(exportPath)) {
            Files.createDirectories(exportPath)
            println("Created directory: $exportPath")
        }

        Files.copy(templateStream, exportPath, StandardCopyOption.REPLACE_EXISTING)

        val libPath: Path = exportPath.resolve("lib")
        Files.createDirectory(libPath)
        modifyPubSpecFile(exportPath, versionString)

        generators.forEach { generator -> generator.generate(libPath) }

    }
}