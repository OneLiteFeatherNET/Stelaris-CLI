package net.theevilreaper.stelaris.cli.generator

import java.nio.file.Path

/**
 * The interface defines the basic structure for code generators in the Stelaris CLI.
 * All generator implementations must follow this contract.
 *
 * An implementation is registered by annotating it with `@AutoService(Generator::class)` and
 * [CodeGenerator]. The name and the experimental flag live on the annotation and are exposed
 * through the [GeneratorDescriptor], not through the generator itself.
 * @author Joltras
 * @version 1.0.0
 * @since 1.0.0
 */
interface Generator {

    /**
     * Executes the generation process with the implementation-specific logic.
     * @param outputPath the output directory path where the generated files should be written
     */
    fun generate(outputPath: Path)

    /**
     * Cleans up any resources or state data used by the generator.
     * This should be called after the generation is complete.
     */
    fun cleanUp()
}
