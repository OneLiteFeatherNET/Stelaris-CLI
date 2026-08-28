package net.theevilreaper.stelaris.cli.generator

import jakarta.inject.Inject
import jakarta.inject.Singleton

/**
 * The [GeneratorRegistry] holds a descriptor for every available generator.
 *
 * The descriptors are contributed by the [GeneratorModule] and carry the metadata of each
 * generator. Selecting generators therefore does not require creating them: only the descriptors
 * which pass the given filter are turned into instances.
 * @version 1.0.0
 * @since 1.0.0
 * @property descriptors the descriptors of all available generators
 * @author theEvilReaper
 */
@Singleton
class GeneratorRegistry @Inject constructor(
    private val descriptors: Set<@JvmSuppressWildcards GeneratorDescriptor>,
) {

    init {
        check(descriptors.isNotEmpty()) {
            "No generators were discovered. When running from the shadow jar this usually means " +
                "mergeServiceFiles() is missing from the shadowJar task"
        }
        val duplicates = descriptors.groupBy { it.name }.filterValues { it.size > 1 }.keys
        check(duplicates.isEmpty()) { "Duplicate generator names: ${duplicates.joinToString()}" }
    }

    /**
     * Returns the descriptors of all available generators.
     * @return a set of all available descriptors
     */
    fun getDescriptors(): Set<GeneratorDescriptor> = descriptors

    /**
     * Creates the generators whose descriptor matches the given predicate.
     * @param predicate the predicate to filter the descriptors
     * @return a set with an instance of every matching generator
     */
    fun createGenerators(predicate: (GeneratorDescriptor) -> Boolean = { true }): Set<Generator> {
        return descriptors.filter(predicate).map { it.create() }.toSet()
    }
}
