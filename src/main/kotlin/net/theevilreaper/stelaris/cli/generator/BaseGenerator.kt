package net.theevilreaper.stelaris.cli.generator

import net.theevilreaper.dartpoet.DartFile
import java.nio.file.Files

import java.nio.file.Path

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
abstract class BaseGenerator(
    val className: String,
    val packageName: String,
) : Generator {

    /**
     * Clears the internal file cache.
     */
    override fun cleanUp() {
    }

    /**
     * Checks if a given package folder exists, if not, it creates it.
     * @param outputPath the base [Path] where the package folder should be located
     * @param packageName the name of the package
     * @return the [Path] of the package folder
     */
    protected fun checkPackageFolder(outputPath: Path, packageName: String): Path {
        val packageFolder = outputPath.resolve(packageName)
        if (!Files.exists(packageFolder)) {
            Files.createDirectories(packageFolder)
        }
        return packageFolder
    }

    /**
     * Contains the logic of what happens during the generation.
     * @param outputPath the [Path] where the files should be generated
     */
    abstract override fun generate(outputPath: Path)

}
