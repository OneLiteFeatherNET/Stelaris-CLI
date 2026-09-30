package net.theevilreaper.stelaris.cli.generator.dart.component

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ComponentSchemaTest {

    @Test
    fun `test dart rendering of simple schemas`() {
        assertEquals("IntSchema()", IntSchema().toDart())
        assertEquals("IntSchema(min: 1, max: 99)", IntSchema(1, 99).toDart())
        assertEquals("FloatSchema(min: 0.0)", FloatSchema(min = 0.0).toDart())
        assertEquals("KeySchema(registry: 'item')", KeySchema("item").toDart())
        assertEquals("EnumSchema(['common', 'rare'])", EnumSchema(listOf("common", "rare")).toDart())
        assertEquals("ListSchema(TextSchema(), maxLength: 256)", ListSchema(TextSchema, 256).toDart())
        assertEquals("UnsupportedSchema('Map<String, String>')", UnsupportedSchema("Map<String, String>").toDart())
    }

    @Test
    fun `test dart rendering of an object schema`() {
        val schema = ObjectSchema(
            linkedMapOf(
                "nutrition" to ComponentField(IntSchema(min = 0)),
                "can_always_eat" to ComponentField(BoolSchema, optional = true),
            )
        )
        assertEquals(
            "ObjectSchema({'nutrition': ComponentField(IntSchema(min: 0)), " +
                "'can_always_eat': ComponentField(BoolSchema(), optional: true)})",
            schema.toDart()
        )
    }

    @Test
    fun `test dart rendering of a spec`() {
        assertEquals(
            "ComponentSpec('minecraft:lore', 'LORE', TextSchema(), managed: true)",
            ComponentSpec("minecraft:lore", "LORE", TextSchema, managed = true).toDart()
        )
        assertEquals(
            "ComponentSpec('minecraft:container', 'CONTAINER', UnitSchema(), editable: false)",
            ComponentSpec("minecraft:container", "CONTAINER", UnitSchema, editable = false).toDart()
        )
    }

    @Test
    fun `test unsupported parts are collected`() {
        val schema = ObjectSchema(
            mapOf(
                "a" to ComponentField(ListSchema(UnsupportedSchema("A"))),
                "b" to ComponentField(IntSchema()),
            )
        )
        assertEquals(listOf(UnsupportedSchema("A")), schema.unsupported())
        assertTrue(IntSchema().unsupported().isEmpty())
    }
}
