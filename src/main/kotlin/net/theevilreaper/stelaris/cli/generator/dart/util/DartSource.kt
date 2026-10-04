package net.theevilreaper.stelaris.cli.generator.dart.util

import java.nio.file.Files
import java.nio.file.Path

/**
 * Helpers for Dart files which are written as plain source instead of through DartPoet.
 *
 * This is used for files that consist of const collections with nested constructor calls,
 * which are easier to express as a template.
 * @since 1.0.0
 */
object DartSource {

    /**
     * The header on top of every generated file.
     */
    const val GENERATED_HEADER: String = "// The file is generated. Don't change anything here"

    /**
     * Renders a value as a single-quoted Dart string literal.
     * @param value the value to render
     * @return the Dart string literal
     */
    fun string(value: String): String {
        val escaped = value
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("$", "\\$")
            .replace("\n", "\\n")
        return "'$escaped'"
    }

    /**
     * Writes a Dart file into the given folder.
     * @param folder the folder of the file
     * @param fileName the name of the file without the `.dart` extension
     * @param content the content of the file
     * @return the [Path] of the written file
     */
    fun write(folder: Path, fileName: String, content: String): Path {
        val file = folder.resolve("$fileName.dart")
        Files.writeString(file, if (content.endsWith("\n")) content else "$content\n")
        return file
    }
}
