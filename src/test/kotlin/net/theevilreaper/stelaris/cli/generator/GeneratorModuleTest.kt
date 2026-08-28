package net.theevilreaper.stelaris.cli.generator

import com.google.common.reflect.ClassPath
import com.google.inject.Guice
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GeneratorModuleTest {

    private val registry = Guice.createInjector(GeneratorModule())
        .getInstance(GeneratorRegistry::class.java)

    @Test
    fun `test generators are discovered through the service file`() {
        assertTrue(registry.getDescriptors().isNotEmpty(), "The service file should list every generator")
    }

    @Test
    fun `test every descriptor carries a usable name`() {
        val names = registry.getDescriptors().map { it.name }
        names.forEach { assertTrue(it.isNotBlank(), "A generator name must not be blank") }
        assertEquals(names.size, names.toSet().size, "Generator names must be unique")
    }

    @Test
    fun `test every descriptor can create its generator`() {
        registry.getDescriptors().forEach { descriptor ->
            assertNotNull(descriptor.create(), "Descriptor $descriptor should create a generator")
        }
    }

    /**
     * Guards the one weakness of service based discovery: a generator whose `@AutoService`
     * annotation is missing is silently left out of the service file. The compiler cannot catch
     * that, so the test compares every implementation on the classpath against the discovered set.
     */
    @Test
    @Suppress("UnstableApiUsage")
    fun `test every generator implementation is registered`() {
        val discovered = registry.getDescriptors().map { it.name }.toSet()

        val unregistered = ClassPath.from(javaClass.classLoader)
            .getTopLevelClassesRecursive(GENERATOR_PACKAGE)
            .filter { it.simpleName.endsWith(GENERATOR_SUFFIX) }
            .map { it.load() }
            .filter { Generator::class.java.isAssignableFrom(it) }
            .filter { type ->
                val metadata = type.getAnnotation(CodeGenerator::class.java)
                metadata == null || metadata.name !in discovered
            }
            .map { it.name }

        assertTrue(
            unregistered.isEmpty(),
            "Missing @AutoService or @CodeGenerator on: ${unregistered.joinToString()}",
        )
    }

    private companion object {
        const val GENERATOR_PACKAGE = "net.theevilreaper.stelaris.cli.generator.dart"
        const val GENERATOR_SUFFIX = "Generator"
    }
}
