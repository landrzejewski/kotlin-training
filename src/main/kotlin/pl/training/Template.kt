package pl.training

class Template(private val text: String) {

    private val expressionStart = "#\\{"
    private val expressionEnd = "}"
    private val expression = ".*$expressionStart\\w+$expressionEnd.*".toRegex()
    private val invalidParameter = "\\W+".toRegex()

    fun evaluate(parameters: Map<String, String>): String {
        validate(parameters)
        val result = substitute(parameters)
        validate(result)
        return result
    }

    private fun validate(parameters: Map<String, String>) {
        if (parameters.values.any { it.matches(invalidParameter) }) {
            throw IllegalArgumentException()
        }
    }

    private fun substitute(parameters: Map<String, String>): String {
        var result = text
        for ((key, value) in parameters) {
            result = regex(key).replace(result, value)
        }
        return result
    }

    private fun validate(result: String) {
        if (result.matches(expression)) {
            throw IllegalArgumentException()
        }
    }

    private fun regex(key: String) = "$expressionStart$key$expressionEnd".toRegex()

}
