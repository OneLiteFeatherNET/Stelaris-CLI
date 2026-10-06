package net.theevilreaper.stelaris.cli.generator.dart.component

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minestom.server.MinecraftServer
import net.minestom.server.codec.Result
import net.minestom.server.codec.Transcoder
import net.minestom.server.component.DataComponent
import net.minestom.server.registry.RegistryTranscoder
import net.minestom.testing.Env
import net.minestom.testing.extension.MicrotusExtension
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Checks that a value built from the schema of a component can be read by the codec of Minestom.
 *
 * The UI builds the values from the schema and the generator reads them with the codecs, so both have to
 * agree. For every component which the UI offers, a value with only the required fields and one with all
 * editable fields are decoded.
 */
@ExtendWith(MicrotusExtension::class)
class ComponentSchemaCodecTest {

    /**
     * An existing key for every registry the schema references, the codecs reject unknown keys.
     */
    private val sampleKeys = mapOf(
        "item" to "minecraft:stone",
        "block" to "minecraft:stone",
        "sound_event" to "minecraft:entity.pig.ambient",
        "damage_type" to "minecraft:fall",
        "trim_material" to "minecraft:gold",
        "trim_pattern" to "minecraft:coast",
        "mob_effect" to "minecraft:speed",
        "potion" to "minecraft:swiftness",
        "banner_pattern" to "minecraft:creeper",
        "attribute" to "minecraft:attack_damage",
        "recipe" to "minecraft:stick",
        "painting_variant" to "minecraft:kebab",
        "jukebox_song" to "minecraft:cat",
        "instrument" to "minecraft:ponder_goat_horn",
        "entity_type" to "minecraft:pig",
        "data_component_type" to "minecraft:food",
        "cat_variant" to "minecraft:tabby",
        "cat_sound_variant" to "minecraft:classic",
        "chicken_variant" to "minecraft:temperate",
        "chicken_sound_variant" to "minecraft:classic",
        "cow_variant" to "minecraft:temperate",
        "cow_sound_variant" to "minecraft:classic",
        "frog_variant" to "minecraft:temperate",
        "pig_variant" to "minecraft:temperate",
        "pig_sound_variant" to "minecraft:classic",
        "wolf_variant" to "minecraft:pale",
        "wolf_sound_variant" to "minecraft:classic",
        "zombie_nautilus_variant" to "minecraft:temperate",
    )

    @Test
    fun `test schema values are readable by the codecs`(env: Env) {
        val transcoder = RegistryTranscoder(Transcoder.JSON, MinecraftServer.process())
        val failures = DataComponentCatalog().specs()
            .filter { it.editable }
            // A required part the UI can't edit means the UI can't create the component at all
            .filterNot { sample(it.schema, withOptional = false) == null }
            .mapNotNull { spec ->
                val codec = DataComponent.fromKey(spec.key)?.codec() ?: return@mapNotNull "${spec.key}: no codec"
                listOf(false, true).firstNotNullOfOrNull { withOptional ->
                    val json = sample(spec.schema, withOptional)
                    val error = try {
                        (codec.decode(transcoder, json) as? Result.Error<*>)?.message()
                    } catch (exception: Exception) {
                        exception.toString()
                    }
                    error?.let { "${spec.key}: $it, value $json" }
                }
            }
        assertTrue(failures.isEmpty(), "Values the codecs can't read:\n${failures.joinToString("\n")}")
    }

    /**
     * Builds a value like the UI does: required fields always, optional fields only with [withOptional].
     * @return the value or null if a required part can't be described
     */
    private fun sample(schema: ComponentSchema, withOptional: Boolean): JsonElement? = when (schema) {
        is IntSchema -> JsonPrimitive((schema.min ?: 1).coerceAtLeast(1).coerceAtMost(schema.max ?: Int.MAX_VALUE))
        is FloatSchema -> JsonPrimitive((schema.min ?: 1.0).coerceAtLeast(0.5).coerceAtMost(schema.max ?: Double.MAX_VALUE))
        BoolSchema -> JsonPrimitive(true)
        UnitSchema -> JsonObject()
        StringSchema, TextSchema -> JsonPrimitive("test")
        ColorSchema -> JsonPrimitive(0xFF0000)
        is KeySchema -> JsonPrimitive(sampleKeys[schema.registry] ?: "minecraft:stone")
        is EnumSchema -> JsonPrimitive(schema.values.first())
        is ListSchema -> sample(schema.element, withOptional)?.let { element -> JsonArray().apply { add(element) } }
        is ObjectSchema -> {
            val value = JsonObject()
            schema.fields.forEach { (name, field) ->
                val fieldValue = sample(field.schema, withOptional)
                when {
                    !field.optional -> value.add(name, fieldValue ?: return null)
                    withOptional && fieldValue != null -> value.add(name, fieldValue)
                }
            }
            value
        }
        is UnsupportedSchema -> null
    }
}
