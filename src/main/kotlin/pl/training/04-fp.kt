package pl.training.fn

import pl.training.fn.Stream.Companion.cons
import pl.training.fn.Stream.Companion.empty
import java.time.LocalDateTime

// Pure functions
// - no side effects
// - referential transparency e.g. abs(-3) = 3
// - defined and valid for all possible input values (total vs. partial function)
fun abs(value: Int) = if (value < 0) -value else value

// Currying - transformation of a function with multiple arguments into a sequence of single-argument functions
fun add(value: Int, otherValue: Int) = value + otherValue
fun add(value: Int): (Int) -> Int = { otherValue -> value + otherValue }

/*
    val add3 = add(3)
    println(add3(2))
    println(add3(3))
 */

// Recursion
fun factorial(number: Int): Int {
    tailrec fun loop(currentNumber: Int, accumulator: Int): Int =
        if (currentNumber <= 0) accumulator
        else loop(currentNumber - 1, currentNumber * accumulator)
    return loop(number, 1)
}

fun fibonacci(elementIndex: Int): Int {
    tailrec fun loop(elementsLeft: Int, current: Int, next: Int): Int =
        if (elementsLeft == 0) current
        else loop(elementsLeft - 1, next, current + next)
    return loop(elementIndex, 0, 1)
}

// Higher-order functions
fun formatResult(n: Int, f: (Int) -> Int) = "Result: ${f(n)}"

/*
    println(formatResult(3, ::factorial))
    println(formatResult(3, ::fibonacci))
*/

// Polymorphic functions
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

/*
    val numbers = arrayOf(1, 2, 3, 4, 5, 6)
    println(findFirst(numbers) { it > 2 })
    println(findFirst(numbers, ::isEven))
 */

fun <A, B, C> partial(a: A, fn: (A, B) -> C): (B) -> C = { b -> fn(a, b) }
fun <A, B, C> curry(fn: (A, B) -> C): (A) -> (B) -> C = { a: A -> { b: B -> fn(a, b) } }
fun <A, B, C> uncurry(fn: (A) -> (B) -> C): (A, B) -> C = { a: A, b: B -> fn(a)(b) }
fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { a: A -> f(g(a)) }

/*
    val findInNumbers = partial(arrayOf(1, 2, 3, 4), ::findFirst)
    println(findInNumbers { it == 2 })
    println(findInNumbers { it == 3 })

    val curriedAdd = curry(::add)
    val add3 = curriedAdd(3)
    println(add3(3))

    val uncurriedAdd = uncurry(curriedAdd)
    println(uncurriedAdd(3, 4))

    val absAdd3 = compose(add3, ::abs) //add3(abs(-3))
    println(formatResult(3, absAdd3))
 */

// Functional data structures

sealed class List<out A> {

    companion object {

        fun <A> of(vararg xs: A): List<A> {
            val tail = xs.sliceArray(1 until xs.size)
            return if (xs.isEmpty()) Nil else Cons(xs[0], of(*tail))
        }

        fun <A> empty(): List<A> = Nil

    }

}

object Nil : List<Nothing>()
data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()

/*
    val emptyList = Nil
    val messages = Cons("java", Cons("kotlin", Nil))
 */

fun sum(xs: List<Int>): Int = when (xs) {
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

fun <A> setHead(xs: List<A>, x: A): List<A> = when (xs) {
    is Nil -> Nil
    is Cons -> Cons(x, xs.tail)
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

fun <A> init(l: List<A>): List<A> = when (l) {
    is Cons -> if (l.tail == Nil) Nil else Cons(l.head, init(l.tail))
    is Nil -> Nil
}

fun <A, B> foldRight(xs: List<A>, value: B, f: (A, B) -> B): B = when (xs) {
    is Nil -> value
    is Cons -> f(xs.head, foldRight(xs.tail, value, f))
}

fun sumFr(xs: List<Int>) = foldRight(xs, 0) { a, b -> a + b }
fun productFr(xs: List<Int>) = foldRight(xs, 1.0) { a, b -> a * b }


fun lengthFr(xs: List<Int>) = foldRight(xs, 0) { _, len -> 1 + len }
fun <A> appendFr(a1: List<A>, a2: List<A>): List<A> = foldRight(a1, a2) { x, y -> Cons(x, y) }
fun <A> concatFr(xxs: List<List<A>>): List<A> =
    foldRight(xxs, List.empty()) { xs1: List<A>, xs2: List<A> ->
        foldRight(xs1, xs2) { a, ls -> Cons(a, ls) }
    }


tailrec fun <A, B> foldLeft(xs: List<A>, value: B, f: (B, A) -> B): B = when (xs) {
    is Nil -> value
    is Cons -> foldLeft(xs.tail, f(value, xs.head), f)
}

fun sumFl(xs: List<Int>) = foldLeft(xs, 0) { a, b -> a + b }
fun productFl(xs: List<Int>) = foldLeft(xs, 1.0) { a, b -> a * b }
fun lengthFl(xs: List<Int>) = foldLeft(xs, 0) { len, _ -> 1 + len }

fun <A> reverseFl(xs: List<A>): List<A> = foldLeft(xs, List.empty()) { t: List<A>, h: A -> Cons(h, t) }

fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
    foldRight(xs, { b: B -> b }, { a, g -> { b -> g(f(b, a)) } })(z)

fun <A, B> foldRightL(xs: List<A>, z: B, f: (A, B) -> B): B =
    foldLeft(xs, { b: B -> b }, { g, a -> { b -> g(f(a, b)) } })(z)

fun <A, B> map(xs: List<A>, f: (A) -> B): List<B> = foldRightL(xs, List.empty()) { a: A, xa: List<B> ->
    Cons(f(a), xa)
}

fun <A> filter(xs: List<A>, f: (A) -> Boolean): List<A> = foldRight(xs, List.empty()) { a, ls ->
    if (f(a)) Cons(a, ls)
    else ls
}

fun <A, B> flatMap(xa: List<A>, f: (A) -> List<B>): List<B> =
    foldRight(xa, List.empty()) { a, lb ->
        append(f(a), lb)
    }

fun <A> zipWith(xa: List<A>, xb: List<A>, f: (A, A) -> A): List<A> = when (xa) {
    is Nil -> Nil
    is Cons -> when (xb) {
        is Nil -> Nil
        is Cons -> Cons(f(xa.head, xb.head), zipWith(xa.tail, xb.tail, f))
    }
}

fun <A> reverse(xs: List<A>): List<A> = foldLeft(xs, List.empty()) { t: List<A>, h: A -> Cons(h, t) }

/*
    val values = List.of(1, 2, 3, 4, 5)
    println(sum(values))
    println(product(List.of(1.0, 2.0, 3.0)))
 */

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

fun <A> maxDepthF(tree: Tree<A>) = fold(tree, { 0 }, { b1, b2 -> 1 + maxOf(b1, b2) })

fun <A, B> mapF(tree: Tree<A>, f: (A) -> B) =
    fold(tree, { a: A -> Leaf(f(a)) }, { b1: Tree<B>, b2: Tree<B> -> Branch(b1, b2) })

// Handling errors without exceptions
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

/*
 val datetime = getTimestamp()
        .map { it.year }
        .getOrElse { LocalDateTime.MIN }
    println(datetime)
 */

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

/*
    val division = safeDiv(2, 0)
        .map(add2)
        .map(::abs)
        .orElse { Right(0) }
 */

// Non-strict / lazy evaluation

fun <A> lazyIf(condition: Boolean, onTrue: () -> A, onFalse: () -> A): A = if (condition) onTrue() else onFalse()

/*
    val value = 5
    val result = lazyIf((value < 10), { println("a") }, { println("b") })
 */

sealed class Stream<out A> {

    companion object {

        fun <A> of(vararg xs: A): Stream<A> = if (xs.isEmpty()) empty()
        else cons({ xs[0] }, { of(*xs.sliceArray(1 until xs.size)) })

        // Memoization - prevents multiple evaluations of expensive computations by caching the result
        fun <A> cons(hd: () -> A, tl: () -> Stream<A>): Stream<A> {
            val head: A by lazy(hd)
            val tail: Stream<A> by lazy(tl)
            return LazyCons({ head }, { tail })
        }

        fun <A> empty(): Stream<A> = Empty

    }

    fun <B> foldRight(z: () -> B, f: (A, () -> B) -> B): B = when (this) {
        // f is non-strict in its second parameter. If f chooses
        // not to evaluate its second parameter, the traversal will be terminated early
        is LazyCons -> f(this.head()) {
            tail().foldRight(
                z,
                f
            )
        } // If f doesn’t evaluate its second argument, the recursion never occurs.
        is Empty -> z()
    }

    fun exists(p: (A) -> Boolean): Boolean = foldRight({ false }, { a, b -> p(a) || b() })

}

data class LazyCons<out A>(val head: () -> A, val tail: () -> Stream<A>) : Stream<A>()
object Empty : Stream<Nothing>()

fun <A> Stream<A>.head(): Option<A> =
    when (this) {
        is Empty -> None
        is LazyCons -> Some(head()) // evaluates only the head
    }

fun <A> Stream<A>.toList(): List<A> {
    tailrec fun go(xs: Stream<A>, acc: List<A>): List<A> = when (xs) {
        is Empty -> acc
        is LazyCons -> go(xs.tail(), Cons(xs.head(), acc))
    }
    return reverse(go(this, Nil))
}

fun <A> Stream<A>.take(n: Int): Stream<A> {
    fun go(xs: Stream<A>, n: Int): Stream<A> = when (xs) {
        is Empty -> empty()
        is LazyCons -> if (n == 0) empty() else cons(xs.head) { go(xs.tail(), n - 1) }
    }
    return go(this, n)
}

fun <A> Stream<A>.drop(n: Int): Stream<A> {
    tailrec fun go(xs: Stream<A>, n: Int): Stream<A> = when (xs) {
        is Empty -> empty()
        is LazyCons -> if (n == 0) xs else go(xs.tail(), n - 1)
    }
    return go(this, n)
}

fun <A> Stream<A>.takeWhile(p: (A) -> Boolean): Stream<A> =
    when (this) {
        is Empty -> empty()
        is LazyCons -> if (p(this.head())) cons(this.head) { this.tail().takeWhile(p) } else empty()
    }

fun <A> Stream<A>.forAll(p: (A) -> Boolean): Boolean = foldRight({ true }, { a, b -> p(a) && b() })

fun <A> Stream<A>.takeWhileFr(p: (A) -> Boolean): Stream<A> =
    foldRight({ empty() }, { h, t -> if (p(h)) cons({ h }, t) else t() })

fun <A, B> Stream<A>.map(f: (A) -> B): Stream<B> = this.foldRight({ empty<B>() }, { h, t -> cons({ f(h) }, t) })

fun <A> Stream<A>.filter(f: (A) -> Boolean): Stream<A> =
    this.foldRight({ empty<A>() }, { h, t -> if (f(h)) cons({ h }, t) else t() })

fun <A> Stream<A>.append(sa: () -> Stream<A>): Stream<A> = foldRight(sa) { h, t -> cons({ h }, t) }

fun <A, B> Stream<A>.flatMap(f: (A) -> Stream<B>): Stream<B> = foldRight({ empty<B>() }, { h, t -> f(h).append(t) })

/*
    val stream = Stream.of(1, 2, 3, 4, 5)
    println(stream.take(4).toList())
 */

// Infinite streams
fun ones(): Stream<Int> = cons({ 1 }, { ones() })

fun <A> constant(a: A): Stream<A> = cons({ a }, { constant(a) })

fun from(n: Int): Stream<Int> = cons({ n }, { from(n + 1) })

fun fibonacci(): Stream<Int> {
    fun go(curr: Int, nxt: Int): Stream<Int> = cons({ curr }, { go(nxt, curr + nxt) })
    return go(0, 1)
}

/*
    println(ones().take(5).toList())
    println(ones().exists { it % 2 != 0 })
    println(ones().map { it + 1 }.exists { it % 2 == 0 })
    println(ones().takeWhile { it == 1 }.take(6).toList())
 */

// General stream-building function - takes an initial state, and a function for producing
// both the next state and the next value in the generated stream.
// Option is used to indicate when the Stream should be terminated.
// The unfold function is an example of a corecursive function.
// Whereas a recursive function consumes data, a corecursive function produces data.
// Corecursion is also sometimes called guarded recursion, and productivity is sometimes called cotermination.
fun <A, S> unfold(z: S, f: (S) -> Option<Pair<A, S>>): Stream<A> = f(z)
    .map { pair -> cons({ pair.first }, { unfold(pair.second, f) }) }
    .getOrElse { empty() }

fun fibonacciUf(): Stream<Int> = unfold(0 to 1) { (curr, next) -> Some(curr to (next to (curr + next))) }

fun fromUf(n: Int): Stream<Int> = unfold(n) { a -> Some(a to (a + 1)) }

fun <A> constantUf(n: A): Stream<A> = unfold(n) { a -> Some(a to a) }

fun onesUf(): Stream<Int> = unfold(1) { Some(1 to 1) }

fun <A, B> Stream<A>.mapUf(f: (A) -> B): Stream<B> = unfold(this) { s: Stream<A> ->
    when (s) {
        is LazyCons -> Some(f(s.head()) to s.tail())
        else -> None
    }
}

fun <A> Stream<A>.takeUf(n: Int): Stream<A> = unfold(this) { s: Stream<A> ->
    when (s) {
        is LazyCons -> if (n > 0) Some(s.head() to s.tail().take(n - 1)) else None
        else -> None
    }
}

fun <A> Stream<A>.takeWhileUf(p: (A) -> Boolean): Stream<A> = unfold(this) { s: Stream<A> ->
    when (s) {
        is LazyCons -> if (p(s.head())) Some(s.head() to s.tail()) else None
        else -> None
    }
}

// Functional state

fun interface Rnd {

    fun nextInt(): Pair<Int, Rnd>

}

data class SimpleRandom(val seed: Long) : Rnd {

    override fun nextInt(): Pair<Int, Rnd> {
        val newSeed = (seed * 0x5DEECE66DL + 0xBL) and 0xFFFFFFFFFFFFL
        val nextRNG = SimpleRandom(newSeed)
        val n = (newSeed ushr 16).toInt()
        return n to nextRNG
    }

}

fun nonNegativeInt(rnd: Rnd): Pair<Int, Rnd> {
    val (i1, rng2) = rnd.nextInt()
    return (if (i1 < 0) -(i1 + 1) else i1) to rng2
}

fun double(rnd: Rnd): Pair<Double, Rnd> {
    val (i, rng2) = nonNegativeInt(rnd)
    return (i / (Int.MAX_VALUE.toDouble() + 1)) to rng2
}

typealias Rand<A> = (Rnd) -> Pair<A, Rnd>

fun <A> unit(a: A): Rand<A> = { a to it }

fun <A, B> map(s: Rand<A>, f: (A) -> B): Rand<B> = {
    val (a, rng2) = s(it)
    f(a) to rng2
}

fun nonNegativeEven(): Rand<Int> = map(::nonNegativeInt) { it - (it % 2) }

val doubleR: Rand<Double> = map(::nonNegativeInt) {
    it / (Int.MAX_VALUE.toDouble() + 1)
}

fun <A, B, C> zip(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> = { r1: Rnd ->
    val (a, r2) = ra(r1)
    val (b, r3) = rb(r2)
    f(a, b) to r3
}

fun <A, B> both(ra: Rand<A>, rb: Rand<B>): Rand<Pair<A, B>> = zip(ra, rb) { a, b -> a to b }

val intDouble: Rand<Pair<Int, Double>> = both(::nonNegativeInt, ::double)

fun <A, B> flatMap(f: Rand<A>, g: (A) -> Rand<B>): Rand<B> = { rng ->
    val (a, rng2) = f(rng)
    g(a)(rng2)
}

fun nonNegativeIntLessThan(n: Int): Rand<Int> = flatMap(::nonNegativeInt) { i ->
    val mod = i % n
    if (i + (n - 1) - mod >= 0) unit(mod)
    else nonNegativeIntLessThan(n)
}

fun <A, B> mapF(ra: Rand<A>, f: (A) -> B): Rand<B> = flatMap(ra) { a -> unit(f(a)) }

fun <A, B, C> zipF(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> = flatMap(ra) { a ->
    map(rb) { f(a, it) }
}

fun rollDice(): Rand<Int> = map(nonNegativeIntLessThan(6)) { it + 1 }

/*
    val rng = SimpleRandom(10)
    val (n1, rng2) = rng.nextInt()
    println("n1:$n1; rng2:$rng2")
    val (n2, rng3) = rng2.nextInt()
    println("n2:$n2; rng3:$rng3")
    val (n3, rng4) = rng.nextInt()
    println("n3:$n3; rng4:$rng4")
*/

data class State<S, out A>(val run: (S) -> Pair<A, S>) {

    companion object {

        fun <S, A> unit(a: A): State<S, A> = State { s: S -> a to s }

        fun <S, A, B, C> zip(ra: State<S, A>, rb: State<S, B>, f: (A, B) -> C): State<S, C> = ra.flatMap { a ->
            rb.map { b -> f(a, b) }
        }

        fun <S, A> sequence(fs: List<State<S, A>>): State<S, List<A>> = foldRight(fs, unit(List.empty<A>())) { f, acc ->
            zip(f, acc) { h, t -> Cons(h, t) }
        }

    }

    fun <B> map(f: (A) -> B): State<S, B> =
        flatMap { a -> unit<S, B>(f(a)) }

    fun <B> flatMap(f: (A) -> State<S, B>): State<S, B> =
        State { s: S ->
            val (a: A, s2: S) = this.run(s)
            f(a).run(s2)
        }
}

typealias RandS<A> = State<Rnd, A>

// Semigroup

/*
    A semigroup consists of the following:
    - Some type A
    - An associative binary operation combine that takes two values of type A and combines them
      into one: combine(combine(x,y), z) == combine(x, combine(y,z)) for any
      choice of x: A, y: A, or z: A
 */

fun interface Semigroup<A> {
    fun combine(a1: A, a2: A): A
}

// Monoid

/*
    A monoid consists of the following:
    - Some type A
    - An associative binary operation combine that takes two values of type A and combines them
      into one: combine(combine(x,y), z) == combine(x, combine(y,z)) for any
      choice of x: A, y: A, or z: A
    - The value nil: A, which is an identity for that operation: combine(x, nil) == x and
      combine(nil, x) == x for any x: A
 */

interface Monoid<A> : Semigroup<A> {
    val nil: A
}

val stringMonoid = object : Monoid<String> {
    override fun combine(a1: String, a2: String) = a1 + a2
    override val nil = ""
}

fun <A> concatenate(list: kotlin.collections.List<A>, monoid: Monoid<A>): A =
    list.foldRight(monoid.nil, monoid::combine)

// Balanced fold,  allows parallelism
fun <A, B> foldMap(list: kotlin.collections.List<A>, monoid: Monoid<B>, f: (A) -> B): B = when {
    list.size >= 2 -> {
        val (firstPart, secondPart) = list.chunked(list.size / 2)
        monoid.combine(foldMap(firstPart, monoid, f), foldMap(secondPart, monoid, f))
    }

    list.size == 1 -> f(list.first())
    else -> monoid.nil
}

/*
    println(concatenate(listOf("a", "b"), stringMonoid))
 */

// A type constructor is a type that allows another type to be applied to it.
// The result of this construction is called a higher-kinded type.
// Kotlin cannot express higher-kinded types directly so workaround is needed

interface Kind<out F, out A>

// Foldable

/*
    Describes any container like List, tree or option which is foldable
 */

interface Foldable<F> {
    fun <A, B> foldRight(fa: Kind<F, A>, z: B, f: (A, B) -> B): B
    fun <A, B> foldLeft(fa: Kind<F, A>, z: B, f: (B, A) -> B): B
    fun <A, B> foldMap(fa: Kind<F, A>, m: Monoid<B>, f: (A) -> B): B
    fun <A> concatenate(fa: Kind<F, A>, m: Monoid<A>): A = foldLeft(fa, m.nil, m::combine)
}

// If types A and B are both monoids, they can be composed as a new monoid of Pair<A, B>

fun <A, B> productMonoid(ma: Monoid<A>, mb: Monoid<B>): Monoid<Pair<A, B>> =
    object : Monoid<Pair<A, B>> {
        override fun combine(a1: Pair<A, B>, a2: Pair<A, B>): Pair<A, B> =
            ma.combine(a1.first, a2.first) to mb.combine(a1.second, a2.second)

        override val nil: Pair<A, B>
            get() = ma.nil to mb.nil
    }

// Other, more complex combinators are also possible. Multiple operations can be applied simultaneously with composed monoids,
// thus preventing unnecessary list traversal.

// Functor

/*
    Allows one to apply a function to values inside a generic type/container without changing the structure of the type/container
    A functor consists of the following:
    - When performing the mapping operation, if the values in the functor are mapped to themselves, the result will be an unmodified functor.
    - f two sequential mapping operations are performed one after the other using two functions, the result should be the same as a single
      mapping operation with one function that is equivalent to applying the first function to the result of the second.
 */

interface Functor<F> {
    fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B>
}


interface Monad<F> : Functor<F> {
    fun <A> unit(a: A): Kind<F, A>
    fun <A, B> flatMap(fa: Kind<F, A>, f: (A) -> Kind<F, B>): Kind<F, B>
    override fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B> = flatMap(fa) { a -> unit(f(a)) }
    fun <A, B, C> map2(fa: Kind<F, A>, fb: Kind<F, B>, f: (A, B) -> C): Kind<F, C> = flatMap(fa) { a -> map(fb) { b -> f(a, b) } }
}

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
