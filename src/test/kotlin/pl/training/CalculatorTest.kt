package pl.training

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.AdditionalAnswers
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

// private val repository = mock<PaymentRepository> { on { save(any()) } doAnswer AdditionalAnswers.returnsFirstArg<Payment>() }

class CalculatorTest {

    private val sut = Calculator()

    @BeforeEach
    fun beforeEach() {
        println("Before each")
    }

    @AfterEach
    fun afterEach() {
        println("After each")
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            println("Before all")
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            println("After all")
        }

    }

    @Test
    fun given_two_numbers_when_add_then_returns_their_sum() {
        // Given / Arrange
        val leftHandSide = 1.0
        val rightHandSide = 2.0
        // When / Act
        val actual = sut.add(leftHandSide, rightHandSide)
        // Then // Assert
        val expected = 3
        assertEquals(expected, actual)
    }

    @Test
    fun given_divisor_equals_zero_when_divide_then_throws_exception() {
        assertThrows(IllegalArgumentException::class.java) { sut.divide(10.0, 0.0) }
    }

}
