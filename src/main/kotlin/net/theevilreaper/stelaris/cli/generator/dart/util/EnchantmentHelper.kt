package net.theevilreaper.stelaris.cli.generator.dart.util

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec

internal val CLASS_PROPERTIES: Array<PropertySpec>
    get() = arrayOf(
        PropertySpec.builder("displayName", String::class).modifier { DartModifier.FINAL }.annotation(OVERRIDE).build(),
        PropertySpec.builder("key", String::class).modifier { DartModifier.FINAL }.annotation(OVERRIDE).build(),
        PropertySpec.builder("maxLevel", Integer::class).modifier { DartModifier.FINAL }.annotation(OVERRIDE).build(),
    )

internal val CONSTRUCTOR_PARAMETERS: Array<ParameterSpec>
    get() = arrayOf(
        ParameterSpec.positional("displayName").build(),
        ParameterSpec.positional("key").build(),
        ParameterSpec.positional("maxLevel").build(),
    )