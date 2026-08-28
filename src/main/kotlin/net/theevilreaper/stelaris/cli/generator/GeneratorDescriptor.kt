package net.theevilreaper.stelaris.cli.generator

import jakarta.inject.Provider

/**
 * Describes a [Generator] without creating it.
 *
 * The descriptor exposes the metadata from [CodeGenerator] so that generators can be selected
 * before any of them is constructed. The instance is only created when [create] is called.
 * @version 1.0.0
 * @since 1.0.0
 * @property name the identifying name of the generator
 * @property experimental whether the generator may have incomplete functionality
 * @property provider the provider which creates the generator instance
 */
class GeneratorDescriptor(
    val name: String,
    val experimental: Boolean,
    private val provider: Provider<out Generator>,
) {

    /**
     * Creates the generator described by this descriptor.
     * @return the created [Generator]
     */
    fun create(): Generator = provider.get()

    override fun toString() = name
}
