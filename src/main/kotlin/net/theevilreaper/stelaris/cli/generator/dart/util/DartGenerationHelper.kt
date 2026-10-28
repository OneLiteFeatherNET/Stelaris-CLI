package net.theevilreaper.stelaris.cli.generator.dart.util

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec

/**
 * Contains the default parameters and properties for the dart generation.
 * Every generated enum uses `displayName` for the human-readable name and `key` for the identifier from the game.
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

val DEFAULT_PARAMETERS: Array<ParameterSpec>
    get() = arrayOf(
        ParameterSpec.positional("displayName").build(),
        ParameterSpec.positional("key").build()
    )

/**
 * The `@override` annotation for properties which implement a member of an api interface like `Keyed`.
 */
val OVERRIDE: AnnotationSpec = AnnotationSpec.builder("override").build()

val DEFAULT_PROPERTIES: Array<PropertySpec>
    get() = arrayOf(
        PropertySpec.builder("displayName", String::class).modifier { DartModifier.FINAL }.annotation(OVERRIDE).build(),
        PropertySpec.builder("key", String::class).modifier { DartModifier.FINAL }.annotation(OVERRIDE).build()
    )