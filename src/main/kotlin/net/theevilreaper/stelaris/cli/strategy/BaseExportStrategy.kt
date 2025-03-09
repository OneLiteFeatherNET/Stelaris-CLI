package net.theevilreaper.stelaris.cli.strategy

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

abstract class BaseExportStrategy protected constructor() : ExportStrategy {

    protected val templateDir = "template"
    protected val templatePath: Path

    init {
        val templateDirPath = LocalExportStrategy::class.java.classLoader.getResource(templateDir).path
            ?: throw IllegalStateException("Could not find template directory")
        templatePath = Paths.get(templateDirPath)

    }

    /**
     * Modify the pubspec.yaml file in the given source folder to the new version.
     * @param sourceFolder The source folder containing the pubspec.yaml file
     * @param newVersion The new version to set in the pubspec.yaml file
     */
    protected fun modifyPubSpecFile(sourceFolder: Path, newVersion: String) {
        val pubSpecPath = sourceFolder.resolve("pubspec.yaml")
        if (!Files.exists(pubSpecPath)) {
            throw IllegalStateException("Could not find pubspec.yaml in $sourceFolder")
        }

        val content = String(Files.readAllBytes(pubSpecPath))

        // Configure YAML options for formatting
        val options = DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.isPrettyFlow = true

        val yaml = Yaml(options)

        // Parse the YAML content to a mutable map
        @Suppress("UNCHECKED_CAST")
        val pubspecMap = yaml.load(content) as MutableMap<String, Any>

        // Modify the version
        pubspecMap["version"] = newVersion

        // Write the modified content back to the file
        Files.write(pubSpecPath, yaml.dump(pubspecMap).toByteArray())
    }

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