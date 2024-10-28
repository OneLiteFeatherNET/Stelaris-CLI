package net.theevilreaper.stelaris.cli.generator.dart.util

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec

internal val CLASS_PROPERTIES: Array<PropertySpec> = arrayOf(
    PropertySpec.builder("name", String::class).modifier { DartModifier.FINAL }.build(),
    PropertySpec.builder("category", String::class).modifier { DartModifier.FINAL }.build(),
    PropertySpec.builder("minLevel", Integer::class).modifier { DartModifier.FINAL }.build(),
    PropertySpec.builder("maxLevel", Integer::class).modifier { DartModifier.FINAL }.build()
)

internal val CONSTRUCTOR_PARAMETERS: Array<ParameterSpec> = arrayOf(
    ParameterSpec.positional("name").build(),
    ParameterSpec.positional("category").build(),
    ParameterSpec.positional("minLevel").build(),
    ParameterSpec.positional("maxLevel").build()
)