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
        assertEquals("ColorSchema()", ColorSchema.toDart())
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
            "ObjectSchema({'nutrition': ComponentField('Nutrition', IntSchema(min: 0)), " +
                "'can_always_eat': ComponentField('Can Always Eat', BoolSchema(), optional: true)})",
            schema.toDart()
        )
    }

    @Test
    fun `test dart rendering of a spec`() {
        assertEquals(
            "ComponentSpec('minecraft:lore', 'Lore', ComponentCategory.display, 'LORE', TextSchema())",
            ComponentSpec("minecraft:lore", "Lore", ComponentCategory.DISPLAY, "LORE", TextSchema).toDart()
        )
        assertEquals(
            "ComponentSpec('minecraft:cat/variant', 'Cat Variant', ComponentCategory.entityVariant, 'CAT_VARIANT', " +
                "UnitSchema(), editable: false)",
            ComponentSpec(
                "minecraft:cat/variant", "Cat Variant", ComponentCategory.ENTITY_VARIANT, "CAT_VARIANT", UnitSchema,
                editable = false
            ).toDart()
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

    @Test
    fun `test display name is derived from key or field name`() {
        assertEquals("Max Stack Size", displayName("minecraft:max_stack_size"))
        assertEquals("Tropical Fish Pattern Color", displayName("minecraft:tropical_fish/pattern_color"))
        assertEquals("Can Always Eat", displayName("can_always_eat"))
    }
}
