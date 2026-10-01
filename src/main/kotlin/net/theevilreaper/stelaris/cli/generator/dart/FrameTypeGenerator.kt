package net.theevilreaper.stelaris.cli.generator.dart

import com.google.auto.service.AutoService
import net.minestom.server.advancements.FrameType
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
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import net.theevilreaper.stelaris.cli.util.StringHelper
import java.nio.file.Path

@AutoService(Generator::class)
@CodeGenerator(name = "FrameTypeGenerator")
class FrameTypeGenerator : BaseGenerator(
    className = "FrameType",
    packageName = "advancement",
) {
    private val displayEntry: String = "displayName"
    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val enumFile = ClassSpec.enumClass(className)
            .also {
                FrameType.entries.forEach { model ->
                    val name = model.name.lowercase()
                    it.enumProperty(
                        EnumEntrySpec.builder(name)
                            .parameter(EnumParameterSpec.positional("%C", StringHelper.mapDisplayName(name)))
                            .build()
                    )
                }
            }
            .property(PropertySpec.builder(displayEntry, String::class).modifier(DartModifier.FINAL).build())
            .constructor(
                ConstructorSpec.builder(className)
                    .modifier(DartModifier.CONST)
                    .parameter(ParameterSpec.positional(displayEntry).build())
                    .build()
            )
            .endWithNewLine(true)
            .build()
        val file = DartFile.builder("frame_type")
            .type(enumFile)
        file.writeGenerated(folder)
    }
}
