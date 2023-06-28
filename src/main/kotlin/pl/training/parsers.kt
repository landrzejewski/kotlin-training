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

    fun <U> map(f: (T) -> U): Result<U> = when (this) {
        is Failure -> this
        is Success -> Success(f(match), remainder)
    }

    fun <U> flatMap(f: (T, String) -> Result<U>): Result<U> = when (this) {
        is Failure -> this
        is Success -> f(match, remainder)
    }

    fun mapExpected(f: (String) -> String): Result<T> = when (this) {
        is Success -> this
        is Failure -> Failure(f(expected), remainder)
    }

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

/*fun <T1, T2> sequence(firstParser: Parser<T1>, secondParser: Parser<T2>): Parser<Pair<T1, T2>> = { input ->
    when(val firstResult = firstParser(input)) {
        is Failure -> firstResult
        is Success -> when (val secondResult = secondParser(firstResult.remainder)) {
            is Failure -> secondResult
            is Success -> Success(firstResult.match to secondResult.match, secondResult.remainder)
        }
    }
}*/

/*fun <T1, T2> sequence(firstParser: Parser<T1>, secondParser: Parser<T2>): Parser<Pair<T1, T2>> = { input ->
    when(val firstResult = firstParser(input)) {
        is Failure -> firstResult
        is Success -> secondParser(firstResult.remainder).map { firstResult.match to it }
    }
}*/

fun <T1, T2> sequence(firstParser: Parser<T1>, secondParser: Parser<T2>): Parser<Pair<T1, T2>> = { input ->
    firstParser(input).flatMap { firstResult, remainder ->
        secondParser(remainder).map { firstResult to it }
    }
}

infix fun <T1, T2> Parser<T1>.then(otherParser: Parser<T2>): Parser<Pair<T1, T2>> = sequence(this, otherParser)

fun <T> oneOf(firstParser: Parser<T>, secondParser: Parser<T>): Parser<T> = { input ->
    when (val firstResult = firstParser(input)) {
        is Success -> firstResult
        is Failure -> secondParser(input).mapExpected { "${firstResult.expected}, $it" }
    }
}

infix fun <T> Parser<T>.or(otherParser: Parser<T>): Parser<T> = oneOf(this, otherParser)

fun <T, U> Parser<T>.map(f: (T) -> U): Parser<U> = { this(it).map(f) }

infix fun <X, T> Parser<X>.skipLeft(p: Parser<T>): Parser<T> = sequence(this, p).map { it.second }

infix fun <T, Y> Parser<T>.skipRight(y: Parser<Y>): Parser<T> = sequence(this, y).map { it.first  }

fun <T> Parser<T>.many(): Parser<List<T>> = { input ->
    when (val result = this(input)) {
        is Failure -> Success(emptyList(), input)
        is Success -> many()(result.remainder).map { listOf(result.match) + it }
    }
}

infix fun <T, X> Parser<T>.separatedBy(separatorParser: Parser<X>) : Parser<List<T>> = { input ->
    fun parse(tail: String): Result<List<T>> = when (val separatorResult = separatorParser(tail)){
        is Failure -> Success(emptyList(), tail)
        is Success -> when (val result = this(separatorResult.remainder)) {
            is Failure -> result
            is Success -> parse(result.remainder).map { listOf(result.match) + it }
        }
    }
    when (val result = this(input)) {
        is Failure -> Success(emptyList(), input)
        is Success -> parse(result.remainder).map { listOf(result.match) + it }
    }
}

fun prefixWhile(predicate: (Char) -> Boolean): Parser<String> = { input ->
    val match = input.takeWhile(predicate)
    if (match.isNotEmpty()) {
        Success(match, input.dropPrefix(match))
    } else {
        Failure("Expected prefix", input)
    }
}







