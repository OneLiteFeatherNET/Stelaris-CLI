package net.theevilreaper.stelaris.cli.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class StringHelperTest {

    companion object {

        @JvmStatic
        private fun providedDisplayNames() = Stream.of(
            Arguments.of(
                "test name",
                "Test name"
            ),
            Arguments.of(
                "THIS IS A TEST",
                "This is a test"
            )
        )
        @JvmStatic
        private fun providedCamelCaseNames() = Stream.of(
            Arguments.of("test_name", "testName"),
            Arguments.of("HELL_WORLD", "hellWorld"),
            Arguments.of("single", "single"),
            Arguments.of("", ""),
            Arguments.of("multiple__underscores", "multipleUnderscores")
        )
        @JvmStatic
        private fun providedSnakeCaseNames() = Stream.of(
            Arguments.of("maxStackSize", "max_stack_size"),
            Arguments.of("hitboxMargin", "hitbox_margin"),
            Arguments.of("single", "single"),
            Arguments.of("", ""),
            Arguments.of("customRGBColor", "custom_rgb_color")
        )
    }

    @ParameterizedTest(name = "Test display name mapping for {0}")
    @MethodSource("providedDisplayNames")
    fun `test display name mapping`(rawName: String, expected: String) {
        assertEquals(expected, StringHelper.mapDisplayName(rawName))
    }

    @ParameterizedTest(name = "Test camel case conversion for {0}")
    @MethodSource("providedCamelCaseNames")
    fun `test camel case conversion`(rawName: String, expected: String) {
        assertEquals(expected, StringHelper.toLowerCamelCase(rawName))
    }

    @ParameterizedTest(name = "Test snake case conversion for {0}")
    @MethodSource("providedSnakeCaseNames")
    fun `test snake case conversion`(rawName: String, expected: String) {
        assertEquals(expected, StringHelper.toSnakeCase(rawName))
    }
}
