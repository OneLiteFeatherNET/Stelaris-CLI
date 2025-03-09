package net.theevilreaper.stelaris.cli.strategy

enum class ExportMode(
    val identifier: String
) {
    GIT("git"),
    LOCAL("local");

    companion object {

        private val default: ExportMode = LOCAL

        fun fromIdentifier(identifier: String): ExportMode? {
            return entries.find { it.identifier == identifier } ?: default
        }
    }
}