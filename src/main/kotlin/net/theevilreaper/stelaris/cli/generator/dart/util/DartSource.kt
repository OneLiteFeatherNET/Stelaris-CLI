package net.theevilreaper.stelaris.cli.generator.dart.util

/**
 * Helpers for Dart files which are written as plain source instead of through DartPoet.
 *
 * This is used for files that consist of const collections with nested constructor calls,
 * which are easier to express as a template. Such files are written with [writeGenerated].
 * @since 1.0.0
 */
object DartSource {

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
}
