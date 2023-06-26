package pl.training

fun main() {
    var number = 5
    if (number % 2 == 0) {
        println("$number is even")
    } else {
        println("$number is odd")
    }

    // if jest prawdziwym wyrażeniem
    val result: Char = if (5 < 3) 'a' else 'b'

    val value: Any? = null
    when (value) {
        1 -> println("One")
        2 -> println("Two")
        3, 4, 5 -> println("Three, four or five")
        in 6..10 -> println("between 6 and 10")
        // value > 0 -> println("between 6 and 10")
        // is String ->  println("Is text")
        // test() -> println("Is null")
        null -> println("Is null")
        else -> {
            println("Other")
        }
    }

    val numbers = arrayOf(1, 2, 3, 5)
    for (currentNumber in numbers) {
        println("Current number: $currentNumber")
    }

    for ((index, currentNumber) in numbers.withIndex()) {
        println("$index: Current number: $currentNumber")
    }

    for (currentNumber in 1.. 10) {
        println("Current number: $currentNumber")
    }

    for (currentNumber in 10 downTo 0 step 2) {
        println("Current number: $currentNumber")
    }

    while (number > 0) {
        println("Current number: $number")
        number--
    }

    do {
        println("Current number: $number")
        number--
    } while (number > 0)


    firstLoop@ while (number > 0) {
        println("Current number: $number")
        number--
        for (currentNumber in numbers) {
            println("Current number: $currentNumber")
            if (currentNumber == 2) {
                break@firstLoop
            }
        }
    }

}

fun test() = true

fun isEven(value: Int): Boolean = value % 2 == 0

