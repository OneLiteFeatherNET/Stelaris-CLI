package net.theevilreaper.stelaris.cli.generator

import java.nio.file.Path

/**
 * The interface defines the basic structure for code generators in the Stelaris CLI.
 * All generator implementations must follow this contract.
 * @author Joltras
 * @version 1.0.0
 * @since 1.0.0
 */
interface Generator {

    /**
     * Executes the generation process with the implementation-specific logic.
     * @param javaPath the output directory path where the generated files should be written
     */
    fun generate(javaPath: Path)

    /**
     * Returns the name from the generator.
     * This is used to identify the generator in logs and other output.
     * @return the generator's identifying name
     */
    fun getName(): String

    /**
     * Cleans up any resources or state data used by the generator.
     * This should be called after generation is complete.
     */
    fun cleanUp()

    /**
     * Indicates if the generator is considered experimental.
     * Experimental generators may have incomplete functionality or be subject to change.
     * @return true if the generator is experimental, false otherwise
     */
    fun isExperimental(): Boolean
}