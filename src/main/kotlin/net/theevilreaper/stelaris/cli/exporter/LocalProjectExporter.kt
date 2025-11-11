package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.stelaris.cli.generator.Generator
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.zip.ZipInputStream

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
            Files.createDirectory(exportPath)
            println("Created directory: $exportPath")
        }

        println("Exporting to: $exportPath")

        val zipStream = javaClass.getClassLoader().getResourceAsStream("flutter_template.zip")
        ZipInputStream(zipStream).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val newPath = exportPath.resolve(entry.name)

                if (entry.isDirectory) {
                    Files.createDirectories(newPath)
                } else {
                    Files.createDirectories(newPath.parent)
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING)
                }

                zis.closeEntry()
                entry = zis.nextEntry
            }
        }

        val libPath: Path = exportPath.resolve("lib")
        if (!Files.exists(libPath)) Files.createDirectory(libPath)
        modifyPubSpecFile(exportPath, versionString)
        generators.forEach { generator -> generator.generate(libPath) }
    }
}