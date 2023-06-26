package pl.training.fn

import java.time.LocalDate
import java.time.LocalDateTime

// Pure function
// - dla tego samego wejścia zwraca to samo wyjście
// - nie ma efektów ubocznych
// - transparentość referencyjna abs(-3) == 3
fun abs(value: Int) = if (value < 0) -value else value

fun multiAdd(a: Int, b: Int) = a + b
fun add(a: Int): (Int) -> Int = { b -> a + b }

// Rekurencja zamiast pętli
fun factorial(number: Int): Int {
    tailrec fun loop(n: Int, result: Int = 1): Int = if (n <= 0) result else loop(n - 1, n * result)
    return loop(number)
}

// Higher-order function
fun formatResult(value: Int, f: (Int) -> Int) = "Result for value $value equals ${f(value)}"

// Funkcje polimorficzne (generyczne)
typealias Predicate<T> = (T) -> Boolean

fun <E> findFirst(xs: Array<E>, predicate: Predicate<E>): Int {
    tailrec fun loop(index: Int): Int = when {
        index == xs.size -> -1
        predicate(xs[index]) -> index
        else -> loop(index + 1)
    }
    return loop(0)
}

fun isEven(value: Int) = value % 2 == 0

fun <A, B, C> partial(a: A, fn: (A, B) -> C): (B) -> C = { b -> fn(a, b) }
fun <A, B, C> curry(fn: (A, B) -> C): (A) -> (B) -> C = { a: A -> { b: B -> fn(a, b) } }
fun <A, B, C> uncurry(fn: (A) -> (B) -> C): (A, B) -> C = { a: A, b: B -> fn(a)(b) }
fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { a: A -> f(g(a)) }

sealed class Option<out A>
class Some<A>(val value: A) : Option<A>()
object None : Option<Nothing>()

fun <A, B> Option<A>.map(f: (A) -> B) = when (this) {
    is None -> None
    is Some -> Some(f(value))
}

fun <A> Option<A>.getOrElse(default: () -> A): A = when (this) {
    is None -> default()
    is Some -> value
}

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> = map(f).getOrElse { None }

fun <A> Option<A>.orElse(ob: () -> Option<A>): Option<A> = map { Some(it) }.getOrElse { ob() }

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> = flatMap { a -> if (f(a)) Some(a) else None }

fun getTimestamp(): Option<LocalDateTime> = None // Some(LocalDateTime.now())

sealed class Either<out E, out A>
data class Left<out E>(val value: E) : Either<E, Nothing>()
data class Right<out A>(val value: A) : Either<Nothing, A>()

fun <E, A, B> Either<E, A>.map(f: (A) -> B): Either<E, B> = when (this) {
    is Left -> this
    is Right -> Right(f(value))
}

fun <E, A> Either<E, A>.orElse(f: () -> Either<E, A>): Either<E, A> = when (this) {
    is Left -> f()
    is Right -> this
}

fun safeDiv(x: Int, y: Int): Either<String, Int> =
    try {
        Right(x / y)
    } catch (e: Exception) {
        Left("Division by zero")
    }

fun main() {
    val add2 = add(2)
    println(add2(3))
    println(add2(4))

    println("Factorial: ${factorial(5)}")

    println(formatResult(-5, ::abs))
    println(formatResult(5, add(2)))

    val numbers = arrayOf(1, 2, 3, 4, 5, 6)
    findFirst(numbers) { it > 2 }
    findFirst(numbers, ::isEven)

    val add4 = partial(4, ::multiAdd)
    println(add4(4))

    val findInNumbers = partial(arrayOf(1, 2, 3, 4), ::findFirst)
    println(findInNumbers { it == 2 })
    println(findInNumbers { it == 3 })

    val curriedMultiAdd = curry(::multiAdd)
    val add5 = curriedMultiAdd(3)
    println(add5(3))
    println(add5(4))

    val standardAdd = uncurry(curriedMultiAdd)
    println(standardAdd(3, 4))

    val absAdd4 = compose(add4, ::abs) //add4(abs(-3))
    println("Result: ${absAdd4(-5)}")

    /*val optionX: Int? = null
    val res = optionX ?: 4*/

    val datetime = getTimestamp()
        .map { it.year }
        .getOrElse { LocalDateTime.MIN }
    println(datetime)

    val division = safeDiv(2, 0)
        .map(add2)
        .map(::abs)
        .orElse { Right(0) }

}


