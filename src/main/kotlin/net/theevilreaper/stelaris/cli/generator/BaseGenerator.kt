package net.theevilreaper.stelaris.cli.generator

import net.theevilreaper.dartpoet.DartFile
import org.jetbrains.annotations.ApiStatus

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

    private val filesToGenerate: MutableList<DartFile> = arrayListOf()

    private val experimental: Boolean by lazy {
        this.javaClass.isAnnotationPresent(ApiStatus.Experimental::class.java)
    }

    /**
     * Clears the internal file cache.
     */
    override fun cleanUp() {
        this.filesToGenerate.clear()
    }

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

    /**
     * Returns whether the generator is experimental or not.
     * @return true if the generator is experimental, false otherwise
     */
    override fun isExperimental(): Boolean = experimental
}
