package net.theevilreaper.stelaris.cli.strategy

import net.theevilreaper.stelaris.cli.generator.Generator
import java.nio.file.Files
import java.nio.file.Path

class LocalExportStrategy(
    private val exportPath: Path,
    private val generators: List<Generator>
) : BaseExportStrategy() {

    override fun export() {
        println("Exporting to local path: $exportPath")

        if (!Files.exists(exportPath)) {
            Files.createDirectories(exportPath)
            println("Created directory: $exportPath")
        }

        copyResourceFolder(templatePath, exportPath)

        val libPath: Path = exportPath.resolve("lib")
        Files.createDirectory(libPath)
        modifyPubSpecFile(exportPath, "1.20.0")

        generators.forEach { generator -> generator.generate(libPath) }

    }
}