package net.theevilreaper.stelaris.cli.generator.dart.util

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec

internal val CLASS_PROPERTIES: Array<PropertySpec>
    get() = arrayOf(
        PropertySpec.builder("displayName", String::class).modifier { DartModifier.FINAL }.build(),
        PropertySpec.builder("minecraftValue", String::class).modifier { DartModifier.FINAL }.build(),
        PropertySpec.builder("maxLevel", Integer::class).modifier { DartModifier.FINAL }.build(),
    )

internal val CONSTRUCTOR_PARAMETERS: Array<ParameterSpec>
    get() = arrayOf(
        ParameterSpec.positional("displayName").build(),
        ParameterSpec.positional("minecraftValue").build(),
        ParameterSpec.positional("maxLevel").build(),
    )