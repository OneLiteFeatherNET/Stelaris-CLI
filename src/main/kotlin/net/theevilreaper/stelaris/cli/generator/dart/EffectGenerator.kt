package net.theevilreaper.stelaris.cli.generator.dart

import net.minestom.server.effects.Effects
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PARAMETERS
import net.theevilreaper.stelaris.cli.generator.dart.util.DEFAULT_PROPERTIES
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

class EffectGenerator : BaseGenerator(
    className = "Effect",
    packageName = "effect",
) {
    override fun generate(javaPath: Path) {
        val effects = Effects.entries
        val enumEntries = mutableListOf<EnumEntrySpec>()
        effects.forEach {
            enumEntries.add(
                EnumEntrySpec.builder(it.name.lowercase())
                    .parameter(EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(it.name)))
                    .build()
            )
        }

        val enumClass = ClassSpec.enumClass(className)
            .enumProperties(*enumEntries.toTypedArray())
            .property(DEFAULT_PROPERTIES[0])
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameter(DEFAULT_PARAMETERS[0])
                    .build()
            )
            .build()
        val file = DartFile.builder(packageName)
            .doc("Generated class to represent the available effects from the game Minecraft")
            .type(enumClass)
            .build()
        file.write(javaPath)
    }

    override fun getName(): String = "EffectGenerator"

    override fun isExperimental() = false
}
