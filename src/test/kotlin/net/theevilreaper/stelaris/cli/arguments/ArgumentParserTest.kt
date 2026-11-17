package net.theevilreaper.stelaris.cli.arguments

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Path

class ArgumentParserTest {

    private fun parse(vararg args: String): ParsedArgs = ArgumentParser.parse(arrayOf(*args))

    private fun failure(vararg args: String): String =
        assertThrows<InvalidArgumentsException> { parse(*args) }.message!!

    @Test
    fun `test local export`() {
        assertEquals(
            ParsedArgs(showHelp = false, version = "26.2-rev1", experimental = true, localBuild = true, path = Path.of("out")),
            parse("-version", "26.2-rev1", "-type", "local", "-path", "out", "-experimental"),
        )
    }

    @Test
    fun `test local is the default type`() {
        assertEquals(true, parse("-version", "26.2-rev1", "-path", "out").localBuild)
    }

    @Test
    fun `test git export`() {
        assertEquals(
            ParsedArgs(showHelp = false, version = "26.2-rev1", experimental = false, localBuild = false, path = null),
            parse("-version", "26.2-rev1", "-type", "git"),
        )
    }

    @Test
    fun `test help needs no other argument`() {
        assertEquals(true, parse("-help").showHelp)
    }

    @Test
    fun `test unknown type fails`() {
        assertEquals(
            "Unknown type 'gti', use one of: local, git",
            failure("-version", "26.2-rev1", "-type", "gti", "-path", "out"),
        )
    }

    @Test
    fun `test local export without path fails`() {
        assertEquals("A local export needs the folder of the project with -path", failure("-version", "26.2-rev1"))
    }

    @Test
    fun `test git export with path fails`() {
        assertEquals(
            "A git export clones the repository itself and takes no -path",
            failure("-version", "26.2-rev1", "-type", "git", "-path", "out"),
        )
    }

    @Test
    fun `test missing version fails`() {
        assertEquals("Please specify a version with -version", failure("-path", "out"))
    }

    @Test
    fun `test missing value fails`() {
        assertEquals("Missing value for argument -version", failure("-version"))
        assertEquals("Missing value for argument -version", failure("-version", "-experimental"))
    }

    @Test
    fun `test unknown argument fails`() {
        assertEquals("The argument -update is not supported", failure("-update", "rev"))
    }

    @Test
    fun `test value without argument fails`() {
        assertEquals("Unexpected value '26.2-rev1'", failure("26.2-rev1", "-path", "out"))
    }
}
