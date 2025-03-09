package net.theevilreaper.stelaris.cli.exporter

/**
 * Represents the interface for exporting a project.
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
sealed interface ProjectExporter {

    /**
     * Exports the project to the given location.
     */
    fun export()
}