package net.theevilreaper.stelaris.cli.exporter

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

/**
 * The [BaseExporter] class is an additional abstraction layer for exporting projects.
 * It provides some basic functionality for exporting a project to a specific location.
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 * @property pubSpec the name of the pubspec.yaml file
 */
abstract class BaseExporter protected constructor() : ProjectExporter {

    private val pubSpec: String = "pubspec.yaml"

    /**
     * Copy the content of the source folder to the destination folder.
     * @param sourceFolder The source folder to copy
     * @param destinationFolder The destination folder to copy the content to
     */
    protected fun copyResourceFolder(sourceFolder: Path, destinationFolder: Path) {
        Files.list(sourceFolder).use { paths ->
            paths.forEach { path ->
                val destinationPath = destinationFolder.resolve(path.fileName)
                if (Files.isDirectory(path)) {
                    copyResourceFolder(path, destinationPath) // Pass the correct subfolder source
                } else {
                    Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING)
                }
            }
        }
    }
}
