package pl.training

import pl.training.payments.domain.Money
import java.lang.IllegalStateException
import java.math.BigDecimal
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/*class Account constructor(number: String) {

}*/

 fun interface Printable {

    fun print(value: String)

}

open class Account(val number: String) {

    var balance = 0.0

    init {
        println("First init")
    }


    init {
        println("Second init")
    }

    constructor(balance: Double, number: String) : this(number) {
        this.balance = balance
    }

    companion object {

        const val BANK_ID = 1

    }

}

class PremiumAccount(number: String) : Account(number) {

}

abstract class User {

    abstract fun getInfo() : String

}

class Admin : User() {

    override fun getInfo() = "User"

}

fun main() {
    val account = Account("1234566789")
    // account.number = "1234"
    Account.BANK_ID
    account.rest()

    generateReport(object : Printable {
        override fun print(value: String) {
            println(value)
        }
    })

    /*generateReport {
        println(it)
    }*/

    val orderCopy = Order("1", emptyArray())
        .copy(id = "3")

    // orderCopy.component1()

    val (id, products) = orderCopy
    println("Order id $id")

    add(secondValue = 2, value = 1)

    println(lazyValue)
    println(lazyValue)

    Counter().value++

    val person = Person()
    person.name = "Jan"
    println(person.name)

    val money = Money(BigDecimal.ONE, Money.DEFAULT_CURRENCY)
    println(readPropert(money, "currency") as String)
}

fun generateReport(printable: Printable) {
    printable.print("summary")
}

// Extension functions

fun Account.rest() {
    balance = 0.0
}

// Data classes

data class Order(val id: String, val products: Array<String>) {

    val owner = "Jan"

}

// Sealed classes

sealed interface Shape

class Rectangle : Shape

class Circle : Shape



interface Json {

    fun toJson(): String

}

// Wyliczenia

enum class Planet(val mass: Double, val radius: Double): Json {

    EARTH(5.9, 6.3),
    MARS(3.2, 11.1) {

        override fun gravity(): Double = (G * mass / (radius * radius)) * 0.91

    };

    val G = 6.6

    open fun gravity(): Double = G * mass / (radius * radius)

    override fun toJson() = "JSON"

}

open class FlatShape {

    open val area = 20.0.also {
        println("Shape area init")
    }

    init {
        println("Shape init")
    }

    open fun getInfo() = "Shape info"

}

interface Drawable {

    val backgroundColor: String

    fun getBackground() = backgroundColor

    fun getInfo() = "Drawable info"

}

class Square(/*override val backgroundColor: String*/) : FlatShape(), Drawable {

    private var internalColor = "none"

    lateinit var type: String

    override val backgroundColor = "green"

    var color: String = "none"
        // get() = "Blue"
        get() {
            println("Calculating color")
            return "Blue"
        }
        set(value) {
            //internalColor = value
            field = value
        }

    override var area = 10.0.also {
        println("Rectangle area init")
    }

    override fun getInfo() = super<Drawable>.getInfo() + " " + super<FlatShape>.getInfo()

    init {
        println("Rectangle init")
    }

    fun prepare() {
        if (!::type.isInitialized) {
            type = "shape"
        }
    }

    override fun getBackground(): String {
        return "Color: " + super.getBackground()
    }

    companion object {

        const val NUMBER_OF_VERTEX = 4

    }

}

const val VERSION = "1.0"

/* Access modifiers for global elements

If you don't use a visibility modifier, public is used by default, which means that your declarations will be visible everywhere.

If you mark a declaration as private, it will only be visible inside the file that contains the declaration.

If you mark it as internal, it will be visible everywhere in the same module.

The protected modifier is not available for top-level declarations.

 */

/* Access modifiers for class elements

private means that the member is visible inside this class only (including all its members).

protected means that the member has the same visibility as one marked as private, but that it is also visible in subclasses.

internal means that any client inside this module who sees the declaring class sees its internal members.

public means that any client who sees the declaring class sees its public members.

 */


open class Shape2
class Rectangle2: Shape2()

fun Shape2.getName() = "Shape"
fun Rectangle2.getName() = "Rectangle"

fun printClassName(s: Shape2) {
    println(s.getName())
}

fun Int?.toText() = this?.toString() ?: "empty"

val <T> List<T>.lastIndex
    get() = size - 1

class Report {

    companion object {

    }

}

fun Report.Companion.printVersion() {
    println("Version: 1.0")
}

class Outer {

    private val value = "Hello"

    private inner class Nested {

        fun printMessage() {
            println("Message: $value")
        }

    }

    fun doSomeWork() {
        Nested().printMessage()
    }

}

// value
inline class Password(private val text: String) {

}

val lazyValue: String by lazy {
    println("Calculating value")
    "Some value"
}

class Counter {
    var value: Int by Delegates.observable(0) { prop, oldValue, newValue ->
        println("$oldValue => $newValue")
    }
}

class LoggerDelegate<T> {

    private var value: T? = null

    operator fun getValue(owner: Any, property: KProperty<*>): T {
        println("$owner.${property.name} read")
        return value ?: throw IllegalStateException()
    }


    operator fun setValue(owner: Any, property: KProperty<*>, value: T) {
        println("$owner.${property.name} write")
        this.value = value
    }
}

class Person {

    var name: String by LoggerDelegate()

    override fun toString(): String {
        return "Person"
    }

}

@Suppress("UNCHECKED_CAST")
fun <V> readPropert(instance: Any, propertName: String): V {
    val property = instance::class.members
        .first { it.name == propertName } as KProperty1<Any, *>
    return property.get(instance) as V
}

/*
https://kotlinlang.org/docs/inheritance.html#overriding-properties

https://kotlinlang.org/docs/properties.html

https://kotlinlang.https://kotlinlang.org/docs/properties.htmlorg/docs/interfaces.html#properties-in-interfaces

https://kotlinlang.org/docs/visibility-modifiers.html

https://kotlinlang.org/docs/extensions.html#extensions-are-resolved-statically

https://kotlinlang.org/docs/nested-classes.html

https://kotlinlang.org/docs/inline-classes.html

https://kotlinlang.org/docs/operator-overloading.html

https://kotlinlang.org/docs/null-safety.html

https://kotlinlang.org/docs/equality.html

https://kotlinlang.org/docs/scope-functions.html

https://kotlinlang.org/docs/generics.html

https://kotlinlang.org/docs/delegation.html

https://kotlinlang.org/docs/delegated-properties.html

https://kotlinlang.org/docs/object-declarations.html

https://kotlinlang.org/docs/collections-overview.html

https://kotlinlang.org/docs/annotations.html
*/
