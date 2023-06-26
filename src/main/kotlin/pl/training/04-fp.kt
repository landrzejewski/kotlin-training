package pl.training.fn

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

// Struktury danych
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

sealed class List<out A> {

    companion object {

        fun <A> of(vararg xs: A): List<A> {
            val tail = xs.sliceArray(1 until xs.size)
            return if (xs.isEmpty()) Nil else Cons(xs[0], of(*tail))
        }

    }

}
object Nil: List<Nothing>()
data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()

fun sum(xs: List<Int>): Int = when (xs){
    is Nil -> 0
    is Cons -> xs.head + sum(xs.tail)
}

fun product(xs: List<Double>): Double = when (xs) {
    is Nil -> 1.0
    is Cons -> xs.head * product(xs.tail)
}

fun <A> tail(xs: List<A>) = when (xs) {
    is Cons -> xs.tail
    else -> Nil
}

fun <A> prepend(xs: List<A>, x: A) = when (xs) {
    is Cons -> Cons(x, xs)
    else -> Nil
}

fun <A> append(xs1: List<A>, xs2: List<A>): List<A> = when (xs1) {
    is Nil -> xs2
    is Cons -> Cons(xs1.head, append(xs1.tail, xs2))
}

tailrec fun <A> drop(xs: List<A>, n: Int): List<A> =
    if (n <= 0) xs else when (xs) {
        is Cons -> drop(xs.tail, n - 1)
        else -> Nil
    }

tailrec fun <A> dropWhile(xs: List<A>, predicate: Predicate<A>): List<A> = when (xs) {
    is Cons -> if (predicate(xs.head)) dropWhile(xs.tail, predicate) else xs
    else -> xs
}

fun <A, B> foldRight(xs: List<A>, value: B, f: (A, B) -> B): B = when (xs) {
    is Nil -> value
    is Cons -> f(xs.head, foldRight(xs.tail, value, f))
}

fun sumFr(xs: List<Int>) = foldRight(xs, 0) { a, b -> a + b }
fun productFr(xs: List<Int>) = foldRight(xs, 1.0) { a, b -> a * b }
fun lengthFr(xs: List<Int>) = foldRight(xs, 0) { _, len -> 1 + len }

tailrec fun <A, B> foldLeft(xs: List<A>, value: B, f: (B, A) -> B): B = when (xs) {
    is Nil -> value
    is Cons -> foldLeft(xs.tail, f(value, xs.head), f)
}

fun sumFl(xs: List<Int>) = foldLeft(xs, 0) { a, b -> a + b }
fun productFl(xs: List<Int>) = foldLeft(xs, 1.0) { a, b -> a * b }
fun lengthFl(xs: List<Int>) = foldLeft(xs, 0) { len, _ -> 1 + len }

sealed class Tree<out A>
data class Leaf<A>(val value: A) : Tree<A>()
data class Branch<A>(val left: Tree<A>, val right: Tree<A>) : Tree<A>()

fun <A> numberOfNodes(tree: Tree<A>): Int = when (tree) {
    is Leaf -> 1
    is Branch -> 1 + numberOfNodes(tree.left) + numberOfNodes(tree.right)
}

fun <A> maxDepth(tree: Tree<A>): Int = when (tree) {
    is Leaf -> 0
    is Branch -> 1 + maxOf(maxDepth(tree.left), maxDepth(tree.right))
}

fun <A, B> map(tree: Tree<A>, f: (A) -> B): Tree<B> = when (tree) {
    is Leaf -> Leaf(f(tree.value))
    is Branch -> Branch(map(tree.left, f), map(tree.right, f))
}

fun <A, B> fold(tree: Tree<A>, f: (A) -> B, b: (B, B) -> B): B = when (tree) {
    is Leaf -> f(tree.value)
    is Branch -> b(fold(tree.left, f, b), fold(tree.right, f, b))
}

fun <A> numberOfNodeF(tree: Tree<A>) = fold(tree, { 1 }, { b1, b2 -> 1 + b1 + b2 })

fun <A> maxDepthF(tree: Tree<A>) = fold(tree, { 0 }, { b1, b2 -> 1 + maxOf(b1, b2)})

fun <A, B> mapF(tree: Tree<A>, f: (A) -> B) = fold(tree, { a: A -> Leaf(f(a)) }, { b1: Tree<B>, b2: Tree<B> -> Branch(b1, b2)})

// Separation of side effects
fun fahrenheitToCelsius(value: Double): Double = (value - 32) * 5.0 / 9.0
fun toFixed(value: Double) = String.format("%.2f", value)
val convertTemperature = compose(::toFixed, ::fahrenheitToCelsius) // toFixed(fahrenheitToCelsius(x))
fun formatResult(temperature: String) = "Temperature is equal ${temperature}°"

// fun write(text: String): Unit = println(text) // not pure
// fun read(): String = readLine().orEmpty() // not pure

/*fun interface IO {

    fun run()

}

fun write(text: String) = { println(text) }*/


fun interface IO<A> {

    fun run(): A

    fun <B> map(f: (A) -> B): IO<B> = object : IO<B> {

        override fun run(): B = f(this@IO.run())

    }

    fun <B> flatMap(f: (A) -> IO<B>): IO<B> = object : IO<B> {

        override fun run(): B = f(this@IO.run()).run()

    }

    infix fun <B> combine(io: IO<B>): IO<Pair<A, B>> = object : IO<Pair<A, B>> {

        override fun run(): Pair<A, B> = this@IO.run() to io.run()

    }

}

fun read(): IO<String> = IO { readLine().orEmpty() }
fun write(text: String): IO<Unit> = IO { println(text) }

val program = write("Enter temperature in degrees Fahrenheit: ")
    .flatMap { read() }
    .map { it.toDouble() }
    .map(convertTemperature)
    .map(::formatResult)
    .flatMap(::write)

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

    val emptyList = Nil
    val messages = Cons("java", Cons("kotlin", Nil))
    val values = List.of(1, 2, 3, 4, 5)
    println(sum(values))
    println(product(List.of(1.0, 2.0, 3.0)))

    program.run()
}


