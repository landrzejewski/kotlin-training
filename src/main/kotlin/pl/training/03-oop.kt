package pl.training

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

