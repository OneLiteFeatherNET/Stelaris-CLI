package net.theevilreaper.stelaris.cli.generator

import com.google.common.base.CaseFormat
import net.theevilreaper.dartpoet.DartFile

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
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

    private val emptyMessage: String = "No files to write. Skipping the file write"

    private val logger: Logger = LoggerFactory.getLogger(BaseGenerator::class.java)
    private val filesToGenerate: MutableList<DartFile> = arrayListOf()

    protected fun writeFiles(filesList: List<DartFile>, outputFolder: Path) {
        if (filesList.isEmpty()) {
            logger.info(emptyMessage)
            return
        }
        write(filesList, outputFolder)
    }

    protected fun writeFiles(outputFolder: Path) {
        if (filesToGenerate.isEmpty()) {
            logger.info(emptyMessage)
            return
        }
        write(filesToGenerate, outputFolder)
        filesToGenerate.clear()
    }

    private fun write(files: List<DartFile>, outputFolder: Path) {
        files.forEach { file ->
            try {
                file.write(outputFolder)
            } catch (exception: IOException) {
                logger.warn("An error occurred while writing source code to the file system: {0}", exception)
            }
        }
    }

    /**
     * Clears the internal file cache.
     */
    override fun cleanUp() {
        this.filesToGenerate.clear()
    }

    private fun changeFormat(name: String): String = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name)

    /**
     * Contains the logic of what happens during the generation.
     * @param javaPath the [Path] where the files should be generated
     */
    abstract override fun generate(javaPath: Path)

    /**
     * Returns the name from the generator.
     * @return the given name as string
     */
    abstract override fun getName(): String
}
