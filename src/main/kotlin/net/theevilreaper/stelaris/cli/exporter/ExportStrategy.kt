package net.theevilreaper.stelaris.cli.exporter

/**
 * Represents the different strategies for exporting a project.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 * @property identifier the identifier of the strategy
 */
enum class ExportStrategy(
    val identifier: String,
) {
    LOCAL("local"),
    GIT("git")

    ;

    companion object {

        /**
         * Returns the export strategy from the given identifier.
         * @param identifier the identifier of the strategy
         * @return the export strategy or [LOCAL] if the identifier is unknown
         */
        fun fromIdentifier(identifier: String): ExportStrategy {
            return entries.find { it.identifier == identifier } ?: LOCAL
        }
    }
}