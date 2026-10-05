package net.theevilreaper.stelaris.cli.generator.dart.component

import net.minestom.server.component.DataComponent
import net.minestom.server.component.DataComponents
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 * Builds the [ComponentSpec] of every data component which Minestom declares in [DataComponents].
 * @param overrides the hand maintained corrections which are mixed into the derived specs
 * @since 1.0.0
 */
class DataComponentCatalog(private val overrides: ComponentOverrides = ComponentOverrides.VANILLA) {

    private val resolver = ComponentSchemaResolver(overrides)

    /**
     * Reads the static fields of [DataComponents] and derives a spec for each of them.
     * @return the specs, sorted by key
     */
    fun specs(): List<ComponentSpec> = DataComponents::class.java.fields
        .filter { Modifier.isStatic(it.modifiers) && DataComponent::class.java.isAssignableFrom(it.type) }
        .map { field ->
            val component = field.get(null) as DataComponent<*>
            val key = component.key().asString()
            val schema = overrides.componentSchemas[key]?.invoke(resolver)
                ?: resolver.schemaOf((field.genericType as ParameterizedType).actualTypeArguments[0])
            ComponentSpec(
                key = key,
                displayName = overrides.displayNames[key] ?: displayName(key),
                category = overrides.categoryOf(key),
                javaField = field.name,
                schema = schema,
                managed = key in overrides.managed,
                editable = key !in overrides.nonEditable,
            )
        }
        .sortedBy { it.key }
}
