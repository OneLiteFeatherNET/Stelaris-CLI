package net.theevilreaper.stelaris.cli.generator.dart.component

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.RGBLike
import net.minestom.server.registry.Holder
import net.minestom.server.registry.RegistryKey
import net.minestom.server.registry.RegistryTag
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType
import net.minestom.server.utils.Unit as MinestomUnit

/**
 * Derives a [ComponentSchema] from a Java type by reflection.
 *
 * The resolver never fails: whatever it can't describe becomes an [UnsupportedSchema]. Type specific
 * corrections, such as field names which differ from the vanilla format, come from [overrides].
 * @param overrides the hand maintained corrections which are applied during the resolution
 * @since 1.0.0
 */
class ComponentSchemaResolver(private val overrides: ComponentOverrides = ComponentOverrides()) {

    /**
     * Resolves the schema of the given type.
     * @param type the type to resolve
     * @return the resolved schema
     */
    fun schemaOf(type: Type): ComponentSchema = resolve(type, emptySet())

    private fun resolve(type: Type, visiting: Set<Class<*>>): ComponentSchema = when (type) {
        is Class<*> -> resolveClass(type, visiting)
        is ParameterizedType -> resolveParameterized(type, visiting)
        is WildcardType -> type.upperBounds.firstOrNull()?.let { resolve(it, visiting) } ?: unsupported(type)
        is GenericArrayType -> ListSchema(resolve(type.genericComponentType, visiting))
        is TypeVariable<*> -> unsupported(type)
        else -> unsupported(type)
    }

    private fun resolveParameterized(type: ParameterizedType, visiting: Set<Class<*>>): ComponentSchema {
        val raw = type.rawType as Class<*>
        overrides.typeSchemas[raw]?.let { return it }
        val arguments = type.actualTypeArguments
        return when {
            Collection::class.java.isAssignableFrom(raw) -> ListSchema(resolve(arguments[0], visiting))
            // A registry key or holder is written as the key of the entry in the vanilla format
            RegistryKey::class.java.isAssignableFrom(raw) || Holder::class.java.isAssignableFrom(raw) ->
                KeySchema(registryOf(arguments[0]))
            // A tag is written either as '#tag' or as a list of keys, the list covers both
            RegistryTag::class.java.isAssignableFrom(raw) -> ListSchema(KeySchema(registryOf(arguments[0])))
            // The type arguments of a map, record or anything else can't be expressed in a schema
            else -> {
                val resolved = resolveClass(raw, visiting)
                if (resolved is UnsupportedSchema) unsupported(type) else resolved
            }
        }
    }

    private fun resolveClass(type: Class<*>, visiting: Set<Class<*>>): ComponentSchema {
        overrides.typeSchemas[type]?.let { return it }
        return when {
            type in INT_TYPES -> IntSchema()
            type in FLOAT_TYPES -> FloatSchema()
            type == Boolean::class.javaPrimitiveType || type == Boolean::class.javaObjectType -> BoolSchema
            type == String::class.java -> StringSchema
            type == MinestomUnit::class.java -> UnitSchema
            Component::class.java.isAssignableFrom(type) -> TextSchema
            // Colors are written as a single RGB integer in the vanilla format
            RGBLike::class.java.isAssignableFrom(type) -> IntSchema(min = 0, max = 0xFFFFFF)
            Key::class.java.isAssignableFrom(type) -> KeySchema()
            type.isEnum -> EnumSchema(enumValues(type))
            type.isRecord -> resolveRecord(type, visiting)
            type.isArray -> ListSchema(resolve(type.componentType, visiting))
            // Registry entries like materials, blocks or sounds are referenced by their key
            Keyed::class.java.isAssignableFrom(type) -> KeySchema(registryOf(type))
            else -> unsupported(type)
        }
    }

    private fun resolveRecord(type: Class<*>, visiting: Set<Class<*>>): ComponentSchema {
        // A record which contains itself can't be expressed as a finite schema
        if (type in visiting) return unsupported(type)
        val nextVisiting = visiting + type
        val fieldNames = overrides.fieldNames[type].orEmpty()
        val optionalFields = overrides.optionalFields[type].orEmpty()
        val fields = linkedMapOf<String, ComponentField>()
        type.recordComponents.forEach { component ->
            val name = fieldNames[component.name] ?: StringHelper.toSnakeCase(component.name)
            val schema = resolve(component.genericType, nextVisiting)
            // A boxed primitive in a record is the usual way to say that a value can be absent
            val optional = component.name in optionalFields || component.type in BOXED_TYPES
            fields[name] = ComponentField(schema, optional)
        }
        return ObjectSchema(fields)
    }

    private fun enumValues(type: Class<*>): List<String> {
        val renames = overrides.enumValues[type].orEmpty()
        return type.enumConstants.map { constant ->
            val name = (constant as Enum<*>).name
            renames[name] ?: name.lowercase()
        }
    }

    private fun registryOf(type: Type): String? {
        val raw = when (type) {
            is Class<*> -> type
            is ParameterizedType -> type.rawType as Class<*>
            else -> return null
        }
        return overrides.registries[raw] ?: StringHelper.toSnakeCase(raw.simpleName)
    }

    private fun unsupported(type: Type): UnsupportedSchema = UnsupportedSchema(shortTypeName(type))

    private companion object {
        private val INT_TYPES = setOf(
            Int::class.javaPrimitiveType, Int::class.javaObjectType,
            Long::class.javaPrimitiveType, Long::class.javaObjectType,
            Short::class.javaPrimitiveType, Short::class.javaObjectType,
            Byte::class.javaPrimitiveType, Byte::class.javaObjectType,
        )
        private val FLOAT_TYPES = setOf(
            Float::class.javaPrimitiveType, Float::class.javaObjectType,
            Double::class.javaPrimitiveType, Double::class.javaObjectType,
        )
        private val BOXED_TYPES = setOf(
            Int::class.javaObjectType, Long::class.javaObjectType, Short::class.javaObjectType,
            Byte::class.javaObjectType, Float::class.javaObjectType, Double::class.javaObjectType,
            Boolean::class.javaObjectType,
        )
        private val PACKAGE_PREFIX = Regex("""\b[a-z][\w]*\.(?=[\w.]*[A-Z])""")

        /**
         * Removes the package names from a type name, e.g.
         * `java.util.Map<java.lang.String, java.lang.String>` becomes `Map<String, String>`.
         */
        fun shortTypeName(type: Type): String = type.typeName.replace(PACKAGE_PREFIX, "")
    }
}
