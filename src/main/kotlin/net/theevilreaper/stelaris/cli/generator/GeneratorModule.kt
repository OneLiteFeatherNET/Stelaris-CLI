package net.theevilreaper.stelaris.cli.generator

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import java.util.ServiceLoader

/**
 * Binds every discovered [Generator] as a [GeneratorDescriptor].
 *
 * Discovery happens through the service file which the `@AutoService` processor writes at compile
 * time. The [ServiceLoader] is only used to find the classes: [ServiceLoader.Provider.type] returns
 * the class without instantiating it, and the instance itself is created by Guice. That keeps the
 * creation lazy and allows generators to receive injected dependencies later on.
 * @version 1.0.0
 * @since 1.0.0
 */
class GeneratorModule : AbstractModule() {

    override fun configure() {
        val multibinder = Multibinder.newSetBinder(binder(), GeneratorDescriptor::class.java)

        ServiceLoader.load(Generator::class.java, javaClass.classLoader)
            .stream()
            .forEach { service ->
                val type = service.type()
                val metadata = requireNotNull(type.getAnnotation(CodeGenerator::class.java)) {
                    "${type.name} is registered as a Generator service but is not annotated with @CodeGenerator"
                }
                multibinder.addBinding().toInstance(
                    GeneratorDescriptor(metadata.name, metadata.experimental, getProvider(type)),
                )
            }
    }
}
