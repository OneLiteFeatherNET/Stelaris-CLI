package net.theevilreaper.stelaris.cli.util

import net.theevilreaper.stelaris.cli.util.exception.UnknownVersionPartException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("Test different version part operations")
class VersionPartTest {

    @Test
    fun `test version mapping`() {
        assertEquals(VersionPart.PATCH.part, VersionPart.parse("patch").part)
    }

    @ParameterizedTest
    @ValueSource(strings = ["major", "minor", "patch", "rev"])
    fun `test version mapping with parameterized test`(part: String) {
        assertNotNull(VersionPart.parse(part))
    }

    @ParameterizedTest
    @ValueSource(strings = ["main", "build"])
    fun `test invalid mapping`(part: String) {
        assertThrows<UnknownVersionPartException>("Unknown version part: $part") {
            VersionPart.parse(
                part
            )
        }
    }

    @Test
    fun `test mapping with empty string`() {
        assertThrows<UnknownVersionPartException>("The version part is empty") {
            VersionPart.parse("")
        }
    }
}
