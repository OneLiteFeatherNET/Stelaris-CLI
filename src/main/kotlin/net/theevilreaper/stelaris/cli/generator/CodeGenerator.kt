package net.theevilreaper.stelaris.cli.generator

/**
 * Carries the metadata of a [Generator] implementation.
 *
 * The annotation is the single source of truth for the name and the experimental flag of a
 * generator. It is read by the [GeneratorModule] while the injector is built, which allows the
 * [GeneratorRegistry] to filter generators without instantiating them.
 *
 * A generator additionally needs `@AutoService(Generator::class)` to be discovered at all. The
 * annotation processor writes it into the service file which the module reads at startup.
 * @version 1.0.0
 * @since 1.0.0
 * @property name the identifying name of the generator
 * @property experimental whether the generator may have incomplete functionality
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CodeGenerator(
    val name: String,
    val experimental: Boolean = false,
)
