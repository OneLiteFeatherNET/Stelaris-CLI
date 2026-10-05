package net.theevilreaper.stelaris.cli.generator.dart.component

import net.theevilreaper.stelaris.cli.generator.dart.util.DartSource
import net.theevilreaper.stelaris.cli.util.StringHelper

/**
 * The Kotlin side of the schema classes in `component_schema.dart`.
 *
 * Every schema knows how to render itself as a const Dart expression, which is what ends up in the
 * generated catalog. The classes have to stay in sync with the Dart template in the resources.
 * @since 1.0.0
 */
sealed interface ComponentSchema {

    /**
     * Renders the schema as a Dart expression which is valid in a const context.
     * @return the Dart expression
     */
    fun toDart(): String

    /**
     * Collects the [UnsupportedSchema] instances in the schema, including nested ones.
     * @return the unsupported parts, an empty list if the schema is complete
     */
    fun unsupported(): List<UnsupportedSchema> = emptyList()
}

data class IntSchema(val min: Int? = null, val max: Int? = null) : ComponentSchema {
    override fun toDart(): String = "IntSchema(${namedArguments("min" to min, "max" to max)})"
}

data class FloatSchema(val min: Double? = null, val max: Double? = null) : ComponentSchema {
    override fun toDart(): String = "FloatSchema(${namedArguments("min" to min, "max" to max)})"
}

data object BoolSchema : ComponentSchema {
    override fun toDart(): String = "BoolSchema()"
}

data object UnitSchema : ComponentSchema {
    override fun toDart(): String = "UnitSchema()"
}

data object StringSchema : ComponentSchema {
    override fun toDart(): String = "StringSchema()"
}

data object TextSchema : ComponentSchema {
    override fun toDart(): String = "TextSchema()"
}

data class KeySchema(val registry: String? = null) : ComponentSchema {
    override fun toDart(): String =
        "KeySchema(${namedArguments("registry" to registry?.let(DartSource::string))})"
}

data class EnumSchema(val values: List<String>) : ComponentSchema {
    override fun toDart(): String = "EnumSchema([${values.joinToString(", ", transform = DartSource::string)}])"
}

data class ListSchema(val element: ComponentSchema, val maxLength: Int? = null) : ComponentSchema {
    override fun toDart(): String {
        val maxLengthArgument = maxLength?.let { ", maxLength: $it" } ?: ""
        return "ListSchema(${element.toDart()}$maxLengthArgument)"
    }

    override fun unsupported(): List<UnsupportedSchema> = element.unsupported()
}

data class ObjectSchema(val fields: Map<String, ComponentField>) : ComponentSchema {
    override fun toDart(): String {
        val entries = fields.entries.joinToString(", ") { (name, field) -> "${DartSource.string(name)}: ${field.toDart(name)}" }
        return "ObjectSchema({$entries})"
    }

    override fun unsupported(): List<UnsupportedSchema> = fields.values.flatMap { it.schema.unsupported() }
}

data class UnsupportedSchema(val javaType: String) : ComponentSchema {
    override fun toDart(): String = "UnsupportedSchema(${DartSource.string(javaType)})"

    override fun unsupported(): List<UnsupportedSchema> = listOf(this)
}

/**
 * A field of an [ObjectSchema].
 * @property schema the schema of the field value
 * @property optional whether the field can be left out
 */
data class ComponentField(val schema: ComponentSchema, val optional: Boolean = false) {

    /**
     * Renders the field as a Dart expression. The label is derived from the [name] of the field.
     * @param name the name of the field in the vanilla format, e.g. `can_always_eat`
     * @return the Dart expression
     */
    fun toDart(name: String): String {
        val optionalArgument = if (optional) ", optional: true" else ""
        return "ComponentField(${DartSource.string(displayName(name))}, ${schema.toDart()}$optionalArgument)"
    }
}

/**
 * Describes a single data component in the catalog.
 * @property key the key of the component, e.g. `minecraft:max_stack_size`
 * @property displayName the name of the component which can be shown in a user interface
 * @property category the section in which the component is offered
 * @property javaField the name of the constant in Minestom's `DataComponents`
 * @property schema the schema of the component value
 * @property managed whether Stelaris handles the component with a dedicated editor
 * @property editable whether the component can be set by a user, false for runtime state
 */
data class ComponentSpec(
    val key: String,
    val displayName: String,
    val category: ComponentCategory,
    val javaField: String,
    val schema: ComponentSchema,
    val managed: Boolean = false,
    val editable: Boolean = true,
) {
    fun toDart(): String {
        val flags = buildString {
            if (managed) append(", managed: true")
            if (!editable) append(", editable: false")
        }
        return "ComponentSpec(${DartSource.string(key)}, ${DartSource.string(displayName)}, " +
            "ComponentCategory.${category.dartName}, ${DartSource.string(javaField)}, ${schema.toDart()}$flags)"
    }
}

/**
 * Derives a name which can be shown in a user interface from a component key or a field name.
 * The namespace is dropped and the path segments are joined, e.g. `minecraft:cat/sound_variant` becomes
 * `Cat Sound Variant`.
 * @param name the key or field name
 * @return the derived name
 */
fun displayName(name: String): String =
    StringHelper.mapDisplayName(name.substringAfter(':').replace('/', '_'))

private fun namedArguments(vararg arguments: Pair<String, Any?>): String =
    arguments.filter { it.second != null }.joinToString(", ") { (name, value) -> "$name: $value" }
