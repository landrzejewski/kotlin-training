package pl.training.fn

// Pure function
// - dla tego samego wejścia dostaje to samo wyjście
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

// Curring and composition
fun <A, B, C> curry(fn: (A, B) -> C): (A) -> (B) -> C = { a: A -> { b: B -> fn(a, b) } }
fun <A, B, C> uncurry(fn: (A) -> (B) -> C): (A, B) -> C = { a: A, b: B -> fn(a)(b) }

fun main() {
    val add2 = add(2)
    println(add2(3))
    println(add2(4))

    println("Facctorial: ${factorial(5)}")

    println(formatResult(-5, ::abs))
    println(formatResult(5, add(2)))

    val numbers = arrayOf(1, 2, 3, 4, 5, 6)
    findFirst(numbers) { it > 2 }
    findFirst(numbers, ::isEven)

    val curriedMultiAdd = curry(::multiAdd)
    val add3 = curriedMultiAdd(3)
    println(add3(3))
    println(add3(4))
}


