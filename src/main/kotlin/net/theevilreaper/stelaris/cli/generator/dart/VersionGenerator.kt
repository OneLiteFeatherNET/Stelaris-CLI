package net.theevilreaper.stelaris.cli.generator.dart

import com.google.auto.service.AutoService
import net.minestom.server.MinecraftServer
import net.theevilreaper.stelaris.cli.generator.BaseGenerator
import net.theevilreaper.stelaris.cli.generator.CodeGenerator
import net.theevilreaper.stelaris.cli.generator.Generator
import net.theevilreaper.stelaris.cli.generator.dart.util.DartSource
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Path

/**
 * Generates the Minecraft version the data of the library belongs to.
 * @since 1.0.0
 */
@AutoService(Generator::class)
@CodeGenerator(name = "VersionGenerator")
class VersionGenerator(
    private val minecraftVersion: String = MinecraftServer.VERSION_NAME,
) : BaseGenerator(
    className = "vulpesMinecraftVersion",
    packageName = "version",
) {

    override fun generate(outputPath: Path) {
        val folder = checkPackageFolder(outputPath, packageName)
        val source = buildString {
            appendLine("/// The Minecraft version the data of the library belongs to.")
            appendLine("const String $className = ${DartSource.string(minecraftVersion)};")
        }
        writeGenerated(folder, "version", source)
    }
}
