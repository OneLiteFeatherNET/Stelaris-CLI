package net.theevilreaper.stelaris.cli.exporter

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.stelaris.cli.generator.dart.util.GENERATED_FILE_HEADER
import net.theevilreaper.stelaris.cli.generator.dart.util.writeGenerated
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.readLines

/**
 * The [DartLibrary] describes the layout of the generated dart package:
 *
 * ```
 * lib/
 *   <package>.dart      exports everything (generated)
 *   <domain>.dart       exports one domain, e.g. material.dart (generated)
 *   src/api/            handwritten code which is never touched by the cli
 *   src/generated/      the output of all generators
 * ```
 *
 * Only the generated parts are removed before a new generation, so files which no longer get
 * generated don't stay in the package.
 * @param projectFolder the root folder of the dart project
 * @author theEvilReaper
 * @since 1.0.0
 */
class DartLibrary(projectFolder: Path) {

    private val libFolder: Path = projectFolder.resolve(LIB_FOLDER)
    private val sourceFolder: Path = libFolder.resolve(SOURCE_FOLDER)
    private val apiFolder: Path = sourceFolder.resolve(API_FOLDER)
    private val packageName: String = readPackageName(projectFolder.resolve(PUBSPEC_FILE))

    /**
     * The folder where all generators write their files to.
     */
    val generatedFolder: Path = sourceFolder.resolve(GENERATED_FOLDER)

    /**
     * Removes the output of a previous generation and creates an empty [generatedFolder].
     * Handwritten files are left untouched.
     */
    fun clean() {
        generatedFolder.toFile().deleteRecursively()
        if (Files.isDirectory(libFolder)) {
            listSorted(libFolder).filter { isGenerated(it) }.forEach(Files::delete)
        }
        Files.createDirectories(generatedFolder)
    }

    /**
     * Writes a library file for each generated domain and one for the whole package.
     * Each domain library also exports the handwritten api files which are imported by its generated files.
     */
    fun writeLibraries() {
        val domains = listSorted(generatedFolder).filter { it.isDirectory() }
        domains.forEach { writeDomainLibrary(it) }

        val apiExports = if (Files.isDirectory(apiFolder)) {
            listSorted(apiFolder).filter { it.isDartFile() }.map { "$SOURCE_FOLDER/$API_FOLDER/${it.name}" }
        } else {
            emptyList()
        }
        writeLibrary(packageName, apiExports + domains.map { "${it.name}.dart" })
    }

    private fun writeDomainLibrary(domain: Path) {
        val files = Files.walk(domain).use { paths ->
            paths.filter { it.isDartFile() }.sorted().toList()
        }
        val apiExports = files
            .flatMap { file -> file.readLines().mapNotNull { API_IMPORT.find(it)?.groupValues?.get(1) } }
            .distinct()
            .map { "$SOURCE_FOLDER/$API_FOLDER/$it" }
        val generatedExports = files.map { libFolder.relativize(it).invariantSeparatorsPathString }
        writeLibrary(domain.name, apiExports + generatedExports)
    }

    private fun writeLibrary(name: String, exports: List<String>) {
        if (exports.isEmpty()) return
        val directives = exports.sorted().map { DirectiveFactory.create(DirectiveType.EXPORT, it) }
        DartFile.builder(name)
            .directives(*directives.toTypedArray())
            .writeGenerated(libFolder)
    }

    private fun listSorted(folder: Path): List<Path> = Files.list(folder).use { it.sorted().toList() }

    private fun Path.isDartFile(): Boolean = isRegularFile() && extension == DART_EXTENSION

    private fun isGenerated(file: Path): Boolean {
        if (!file.isDartFile()) return false
        val firstLine = Files.newBufferedReader(file).use { it.readLine() } ?: return false
        return firstLine == GENERATED_FILE_HEADER.lineSequence().first()
    }

    private companion object {
        const val LIB_FOLDER = "lib"
        const val SOURCE_FOLDER = "src"
        const val API_FOLDER = "api"
        const val GENERATED_FOLDER = "generated"
        const val PUBSPEC_FILE = "pubspec.yaml"
        const val DART_EXTENSION = "dart"
        const val DEFAULT_PACKAGE_NAME = "vulpes_data"

        /**
         * Matches the relative import of a handwritten api file, e.g. `import '../../api/rgb_color.dart';`.
         */
        val API_IMPORT = Regex("""^import '(?:\.\./)+$API_FOLDER/([\w/]+\.dart)';""")

        private val PACKAGE_NAME = Regex("""^name:\s*([a-z0-9_]+)\s*$""")

        /**
         * Reads the name of the package from the pubspec, so the main library has the same name as the package.
         * @param pubspec the path to the pubspec file
         * @return the name of the package or [DEFAULT_PACKAGE_NAME] if it can't be read
         */
        fun readPackageName(pubspec: Path): String {
            if (!Files.isRegularFile(pubspec)) return DEFAULT_PACKAGE_NAME
            return pubspec.readLines().firstNotNullOfOrNull { PACKAGE_NAME.find(it)?.groupValues?.get(1) }
                ?: DEFAULT_PACKAGE_NAME
        }
    }
}
