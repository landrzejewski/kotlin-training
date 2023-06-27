package pl.training

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TemplateTest {

    private val textWithoutExpressions = "My name is Jan Kowalski"
    private val textWithExpressions = "My name is #{firstName} #{lastName}"

    @Test
    fun given_a_text_without_expressions_when_evaluate_then_returns_the_text() {
        val template = Template(textWithoutExpressions)
        val result = template.evaluate(emptyMap())
        assertEquals(textWithoutExpressions, result)
    }

    @Test
    fun given_a_text_with_expressions_when_evaluate_then_returns_the_text_with_substituted_parameters() {
        val template = Template(textWithExpressions)
        val parameters = mapOf("firstName" to "Jan", "lastName" to "Kowalski")
        val result = template.evaluate(parameters)
        assertEquals(textWithoutExpressions, result)
    }

    @Test
    fun given_a_text_with_expressions_when_evaluate_without_providing_all_parameters_then_throws_an_exception() {
        assertThrows<IllegalArgumentException> { Template(textWithExpressions).evaluate(emptyMap()) }
    }

    @Test
    fun given_a_text_with_expressions_when_evaluate_non_alphanumeric_parameters_then_throws_an_exception() {
        val parameters = mapOf("firstName" to "@", "lastName" to "Kowalski")
        assertThrows<IllegalArgumentException> { Template(textWithExpressions).evaluate(parameters) }
    }

}
