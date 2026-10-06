package net.theevilreaper.stelaris.cli.generator.dart.component

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.minestom.server.item.Material
import net.minestom.server.registry.RegistryKey
import net.minestom.server.registry.RegistryTag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Type
import java.util.stream.Stream
import net.minestom.server.utils.Unit as MinestomUnit

class ComponentSchemaResolverTest {

    enum class SampleMode { FIRST_MODE, SECOND }

    @JvmRecord
    data class SampleRecord(
        val maxCount: Int,
        val speed: Float?,
        val displayName: String,
        val mode: SampleMode,
        val tags: List<String>,
    )

    @JvmRecord
    data class RenamedRecord(val saturationModifier: Float, val canAlwaysEat: Boolean)

    @JvmRecord
    data class RecursiveRecord(val value: Int, val child: RecursiveRecord?)

    class OpaqueType

    @Suppress("unused")
    private class GenericHolder {
        lateinit var strings: List<String>
        lateinit var records: List<SampleRecord>
        lateinit var properties: Map<String, String>
        lateinit var materialKey: RegistryKey<Material>
        lateinit var materialTag: RegistryTag<Material>
    }

    private val resolver = ComponentSchemaResolver()

    companion object {

        @JvmStatic
        private fun primitiveTypes() = Stream.of(
            Arguments.of(Int::class.javaObjectType, IntSchema()),
            Arguments.of(Int::class.javaPrimitiveType, IntSchema()),
            Arguments.of(Long::class.javaPrimitiveType, IntSchema()),
            Arguments.of(Float::class.javaObjectType, FloatSchema()),
            Arguments.of(Double::class.javaPrimitiveType, FloatSchema()),
            Arguments.of(Boolean::class.javaObjectType, BoolSchema),
            Arguments.of(String::class.java, StringSchema),
            Arguments.of(MinestomUnit::class.java, UnitSchema),
            Arguments.of(Component::class.java, TextSchema),
            Arguments.of(Key::class.java, KeySchema()),
        )

        private fun genericType(name: String): Type = GenericHolder::class.java.getDeclaredField(name).genericType
    }

    @ParameterizedTest(name = "Test schema of {0}")
    @MethodSource("primitiveTypes")
    fun `test primitive schema`(type: Type, expected: ComponentSchema) {
        assertEquals(expected, resolver.schemaOf(type))
    }

    @Test
    fun `test enum schema uses lowercase constants`() {
        assertEquals(EnumSchema(listOf("first_mode", "second")), resolver.schemaOf(SampleMode::class.java))
    }

    @Test
    fun `test enum schema applies renamed constants`() {
        val overrides = ComponentOverrides(enumValues = mapOf(SampleMode::class.java to mapOf("SECOND" to "other")))
        val schema = ComponentSchemaResolver(overrides).schemaOf(SampleMode::class.java)
        assertEquals(EnumSchema(listOf("first_mode", "other")), schema)
    }

    @Test
    fun `test record schema`() {
        val expected = ObjectSchema(
            linkedMapOf(
                "max_count" to ComponentField(IntSchema()),
                "speed" to ComponentField(FloatSchema(), optional = true),
                "display_name" to ComponentField(StringSchema),
                "mode" to ComponentField(EnumSchema(listOf("first_mode", "second"))),
                "tags" to ComponentField(ListSchema(StringSchema)),
            )
        )
        val schema = resolver.schemaOf(SampleRecord::class.java)
        assertEquals(expected, schema)
        assertEquals(expected.fields.keys.toList(), (schema as ObjectSchema).fields.keys.toList())
    }

    @Test
    fun `test record schema applies field overrides`() {
        val overrides = ComponentOverrides(
            fieldNames = mapOf(RenamedRecord::class.java to mapOf("saturationModifier" to "saturation")),
            optionalFields = mapOf(RenamedRecord::class.java to setOf("canAlwaysEat")),
        )
        val expected = ObjectSchema(
            mapOf(
                "saturation" to ComponentField(FloatSchema()),
                "can_always_eat" to ComponentField(BoolSchema, optional = true),
            )
        )
        assertEquals(expected, ComponentSchemaResolver(overrides).schemaOf(RenamedRecord::class.java))
    }

    @Test
    fun `test recursive record does not recurse endlessly`() {
        val schema = resolver.schemaOf(RecursiveRecord::class.java)
        val child = (schema as ObjectSchema).fields.getValue("child").schema
        assertInstanceOf(UnsupportedSchema::class.java, child)
    }

    @Test
    fun `test list schema`() {
        assertEquals(ListSchema(StringSchema), resolver.schemaOf(genericType("strings")))
        val records = resolver.schemaOf(genericType("records"))
        assertInstanceOf(ListSchema::class.java, records)
        assertInstanceOf(ObjectSchema::class.java, (records as ListSchema).element)
    }

    @Test
    fun `test registry schemas`() {
        val overrides = ComponentOverrides(registries = mapOf(Material::class.java to "item"))
        val resolver = ComponentSchemaResolver(overrides)
        assertEquals(KeySchema("item"), resolver.schemaOf(genericType("materialKey")))
        assertEquals(RegistryTagSchema("item"), resolver.schemaOf(genericType("materialTag")))
        assertEquals(KeySchema("item"), resolver.schemaOf(Material::class.java))
    }

    @Test
    fun `test unsupported schema`() {
        assertEquals(UnsupportedSchema("ComponentSchemaResolverTest\$OpaqueType"), resolver.schemaOf(OpaqueType::class.java))
        assertEquals(UnsupportedSchema("Map<String, String>"), resolver.schemaOf(genericType("properties")))
    }

    @Test
    fun `test type override replaces the derived schema`() {
        val overrides = ComponentOverrides(typeSchemas = mapOf(OpaqueType::class.java to StringSchema))
        assertEquals(StringSchema, ComponentSchemaResolver(overrides).schemaOf(OpaqueType::class.java))
    }
}
