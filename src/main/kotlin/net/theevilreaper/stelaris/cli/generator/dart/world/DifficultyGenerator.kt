package net.theevilreaper.stelaris.cli.generator.dart.world

import com.google.auto.service.AutoService
import net.minestom.server.world.Difficulty
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

/**
 * Generates the [Difficulty] enum for dart.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
@AutoService(Generator::class)
@CodeGenerator(name = "DifficultyGenerator")
class DifficultyGenerator : BaseGenerator(
    className = "Difficulty",
    packageName = "world",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val enumEntries = mutableListOf<EnumEntrySpec>()

        val sortedEntries = Difficulty.entries.sortedBy { it.id.toInt() }

        for (difficulty in sortedEntries) {
            val variableName = StringHelper.toLowerCamelCase(difficulty.name)
            val displayName = StringHelper.mapDisplayName(difficulty.name)

            enumEntries.add(
                EnumEntrySpec.builder(variableName)
                    .parameter(EnumParameterSpec.positional("%C", displayName))
                    .parameter(EnumParameterSpec.positional("%L", difficulty.id.toInt()))
                    .build()
            )
        }

        val enumClass = ClassSpec.enumClass(className)
            .enumProperties(*enumEntries.toTypedArray())
            .properties(
                PropertySpec.builder("displayName", String::class).modifier(DartModifier.FINAL).build(),
                PropertySpec.builder("id", Int::class).modifier(DartModifier.FINAL).build()
            )
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameters(
                        ParameterSpec.positional("displayName").build(),
                        ParameterSpec.positional("id").build()
                    )
                    .build()
            )
            .build()

        val file = DartFile.builder("difficulty")
            .doc("The file is generated. Don't change anything here")
            .type(enumClass)
            .build()
        file.write(folder)
    }
}
