package net.theevilreaper.stelaris.cli.generator.dart.damage

import net.minestom.server.entity.damage.DamageType
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.util.EMPTY_STRING
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

/**
 * Generates the [DamageType] enum for dart.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
class DamageTypeGenerator : BaseGenerator(
    className = "DamageType",
    packageName = "damage",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val registry = DamageType.createDefaultRegistry()
        val enumEntries = mutableListOf<EnumEntrySpec>()

        val distinctEntries = registry.values()
            .distinctBy { registry.getKey(it)?.name() }
            .sortedBy { registry.getKey(it)?.name() ?: EMPTY_STRING }

        for (damageType in distinctEntries) {
            val key = registry.getKey(damageType)?.name() ?: continue
            val nameWithoutPrefix = key.replace("minecraft:", EMPTY_STRING)
            val variableName = StringHelper.toLowerCamelCase(nameWithoutPrefix)
            val displayName = StringHelper.mapDisplayName(nameWithoutPrefix)

            enumEntries.add(
                EnumEntrySpec.builder(variableName)
                    .parameter(EnumParameterSpec.positional("%C", displayName))
                    .parameter(EnumParameterSpec.positional("%C", key))
                    .parameter(EnumParameterSpec.positional("%C", damageType.messageId()))
                    .parameter(EnumParameterSpec.positional("%L", damageType.exhaustion().toString().toDouble()))
                    .build()
            )
        }

        val enumClass = ClassSpec.enumClass(className)
            .enumProperties(*enumEntries.toTypedArray())
            .properties(
                PropertySpec.builder("displayName", String::class).modifier(DartModifier.FINAL).build(),
                PropertySpec.builder("key", String::class).modifier(DartModifier.FINAL).build(),
                PropertySpec.builder("messageId", String::class).modifier(DartModifier.FINAL).build(),
                PropertySpec.builder("exhaustion", Double::class).modifier(DartModifier.FINAL).build()
            )
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(
                        ParameterSpec.positional("displayName").build(),
                        ParameterSpec.positional("key").build(),
                        ParameterSpec.positional("messageId").build(),
                        ParameterSpec.positional("exhaustion").build()
                    )
                    .build()
            )
            .build()

        val file = DartFile.builder("damage_type")
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        file.write(folder)
    }

    override fun getName() = "DamageTypeGenerator"
}
