package net.theevilreaper.stelaris.cli.arguments

import net.theevilreaper.stelaris.cli.util.VersionPart
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class ParsedArgsTest {

    @Test
    fun `test ParsedArgs creation with all parameters`() {
        val parsedArgs = ParsedArgs(
            showHelp = true,
            versionPart = VersionPart.parse("1.0.0"),
            experimental = true,
            localBuild = false,
            path = Paths.get("/some/path")
        )

        assertEquals(true, parsedArgs.showHelp)
        assertEquals(VersionPart.parse("1.0.0"), parsedArgs.versionPart)
        assertEquals(true, parsedArgs.experimental)
        assertEquals(false, parsedArgs.localBuild)
        assertEquals(Paths.get("/some/path"), parsedArgs.path)
    }

    @Test
    fun `test ParsedArgs creation with default values`() {
        val parsedArgs = ParsedArgs(
            showHelp = false,
            versionPart = null,
            experimental = false,
            localBuild = true,
            path = null
        )

        assertEquals(false, parsedArgs.showHelp)
        assertEquals(null, parsedArgs.versionPart)
        assertEquals(false, parsedArgs.experimental)
        assertEquals(true, parsedArgs.localBuild)
        assertEquals(null, parsedArgs.path)
    }

    @Test
    fun `test ParsedArgs with only essential parameters`() {
        val parsedArgs = ParsedArgs(
            showHelp = false,
            versionPart = null,
            experimental = false,
            localBuild = true,
            path = null
        )

        assertEquals(false, parsedArgs.showHelp)
        assertEquals(null, parsedArgs.versionPart)
        assertEquals(false, parsedArgs.experimental)
        assertEquals(true, parsedArgs.localBuild)
        assertEquals(null, parsedArgs.path)
    }

    @Test
    fun `test ParsedArgs equality`() {
        val args1 = ParsedArgs(
            showHelp = true,
            versionPart = VersionPart.parse("1.0.0"),
            experimental = false,
            localBuild = true,
            path = Paths.get("/path")
        )
        val args2 = ParsedArgs(
            showHelp = true,
            versionPart = VersionPart.parse("1.0.0"),
            experimental = false,
            localBuild = true,
            path = Paths.get("/path")
        )

        assertEquals(args1, args2)
    }

    @Test
    fun `test ParsedArgs inequality`() {
        val args1 = ParsedArgs(
            showHelp = true,
            versionPart = VersionPart.parse("1.0.0"),
            experimental = false,
            localBuild = true,
            path = Paths.get("/path")
        )
        val args2 = ParsedArgs(
            showHelp = false,
            versionPart = VersionPart.parse("1.0.1"),
            experimental = true,
            localBuild = false,
            path = Paths.get("/different/path")
        )

        assertEquals(false, args1 == args2)
    }
}