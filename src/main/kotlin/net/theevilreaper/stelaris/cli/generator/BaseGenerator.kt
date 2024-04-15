package net.theevilreaper.stelaris.cli.generator

import com.google.common.base.CaseFormat
import net.theevilreaper.dartpoet.DartFile

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Path
import javax.lang.model.element.Modifier

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
abstract class BaseGenerator<T>(
    val className: String,
    val packageName: String,
) : Generator {

    private val logger: Logger = LoggerFactory.getLogger(BaseGenerator::class.java)
    protected val defaultModifiers = arrayOf(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
    private val filesToGenerate: MutableList<DartFile> = arrayListOf()
    protected val emptyComponent = "empty()"

    protected fun writeFiles(filesList: List<DartFile>, outputFolder: Path) {
        if (filesList.isEmpty()) {
            logger.info("No files to write. Skipping the file write")
            return
        }
        write(filesList, outputFolder)
    }

    protected fun writeFiles(outputFolder: Path) {
        if (filesToGenerate.isEmpty()) {
            logger.info("No files to write. Skipping the file write")
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

    fun toConstantVarDeclaration(identifier: String?, name: String): String {
        return if (identifier.isNullOrEmpty()) changeFormat(name) else identifier + changeFormat(name)
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
