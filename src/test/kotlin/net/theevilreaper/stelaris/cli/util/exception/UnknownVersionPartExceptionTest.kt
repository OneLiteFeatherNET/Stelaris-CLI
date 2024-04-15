package net.theevilreaper.stelaris.cli.util.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.full.superclasses

class UnknownVersionPartExceptionTest {

    @Test
    fun `test exception creation`() {
        val message = "Test message"
        val exception = UnknownVersionPartException(message)
        assertNotNull(exception)
        assertTrue { exception::class.superclasses.isNotEmpty() }
        assertEquals(message, exception.message)
    }
}