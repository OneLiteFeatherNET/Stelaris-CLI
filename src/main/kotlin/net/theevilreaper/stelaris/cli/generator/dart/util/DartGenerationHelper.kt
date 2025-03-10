package net.theevilreaper.stelaris.cli.generator.dart.util

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec

/**
 * Contains the default parameters and properties for the dart generation.
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

val DEFAULT_PARAMETERS = arrayOf(
    ParameterSpec.positional("displayName").build(),
    ParameterSpec.positional("type").build()
)

val DEFAULT_PROPERTIES = arrayOf(
    PropertySpec.builder("displayName", String::class).modifier { DartModifier.FINAL }.build(),
    PropertySpec.builder("type", String::class).modifier { DartModifier.FINAL }.build()
)