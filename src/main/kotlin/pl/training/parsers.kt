package pl.training

import pl.training.Result.Failure
import pl.training.Result.Success

/*fun keyValuePair(text: String): Map<String, String> {
    val (key, value) = text.split(":")
    return mapOf(key to value)
}*/

fun keyValuePair(text: String) = text
    .split("\n")
    .associate {
        val (key, value) = it.split(":")
        (key to value)
    }

sealed class Result<out T> {
    data class Success<T>(val match: T, var remainder: String): Result<T>()
    data class Failure(val expected: String, var remainder: String): Result<Nothing>()
}

typealias Parser<T> = (String) -> Result<T>

fun String.dropPrefix(prefix: String) = substring(prefix.length)

fun prefix(prefixValue: String): Parser<String> = { input ->
    if (input.startsWith(prefixValue)) {
        Success(prefixValue, input.dropPrefix(prefixValue))
    } else {
        Failure("Expected: \"$prefixValue\" prefix", input)
    }
}

fun int(input: String): Result<Int> {
    val match = input.takeWhile { it.isDigit() }
    return if (match.isNotEmpty()) {
        Success(match.toInt(), input.dropPrefix(match))
    } else {
        Failure("Expected: integer value", input)
    }
}

fun whitespace(input: String): Result<String> {
    val match = input.takeWhile { it.isWhitespace() }
    return if (match.isNotEmpty()) {
        Success(match, input.dropPrefix(match))
    } else {
        Failure("Expected: one or more whitespaces", input)
    }
}



















