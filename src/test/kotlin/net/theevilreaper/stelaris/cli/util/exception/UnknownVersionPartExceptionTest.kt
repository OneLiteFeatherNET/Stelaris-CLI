package net.theevilreaper.stelaris.cli.util.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UnknownVersionPartExceptionTest {

    @Test
    fun `test exception creation`() {
        val message = "Test message"
        val exception = UnknownVersionPartException(message)
        assertNotNull(exception)
        assertEquals(message, exception.message)
    }
}