package net.theevilreaper.stelaris.cli.arguments

import net.theevilreaper.stelaris.cli.util.VersionPart
import java.nio.file.Path

/**
 * Data class representing the parsed command-line arguments.
 *
 * @property showHelp Indicates whether the help message should be displayed.
 * @property versionPart The version part specified by the user, or null if not provided.
 * @property experimental Indicates whether experimental features are enabled.
 * @property localBuild Indicates whether the local build option is selected.
 * @property path The path specified by the user, or null if not provided.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
data class ParsedArgs(
    val showHelp: Boolean,
    val versionPart: VersionPart?,
    val experimental: Boolean,
    val localBuild: Boolean,
    val path: Path?
)
