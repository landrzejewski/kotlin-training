package pl.training

/*class Account constructor(number: String) {

}*/

interface Showalble {

    fun show()

}

 fun interface Printable {

    fun print(value: String)

}


open class Account(val number: String) : Showalble {

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

    override fun show() {
       println("Account with number $number")
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
    account.show()
    Account.BANK_ID
    account.rest()

    val testShow = object : Showalble {

        override fun show() {
            TODO("Not yet implemented")
        }

    }

    arrayOf("hi", "hello", "test")
        .forEach { println(it) }


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

fun Account.rest() {
    balance = 0.0
}

data class Order(val id: String, val products: Array<String>) {

    val owner = "Jan"

}

sealed interface Shape

class Rectangle : Shape

class Circle : Shape

enum class Direction {


    NORTH {

          override fun show() {

          }

    },

    SOUTH, WEST, EAST;


    open fun show() {

    }

}

enum class Color(val rgb: Int) {
    RED(12),
    GREEN(200),
    BLUE(100);
}

typealias Predicate<T> = (T) -> Boolean

fun add(value: Int, secondValue: Int = 1) {

}
