package net.theevilreaper.stelaris.cli.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class CommandArgumentTest {

    @ParameterizedTest
    @ValueSource(strings = ["help", "update", "experimental"])
    fun `test valid argument mapping`(argument: String) {
        assertNotNull(CommandArgument.fromIdentifier(argument))
    }

    @ParameterizedTest
    @ValueSource(strings = ["test", "invalid", "argument"])
    fun `test invalid argument mapping`(argument: String) {
        assertNull(CommandArgument.fromIdentifier(argument))
    }
}