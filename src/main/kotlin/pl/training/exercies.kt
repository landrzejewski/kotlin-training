package pl.training

// Napisz funkcję zliczającą ilość wystąpień danej litery w słowie/zdaniu

fun letters() {
    val letter = 'a'
    val text = "Ala ma kota"
    // var result = 0
    /*for (char in text) {
        if (char == letter) {
            result++
        }
    }*/

    // text.forEach { if (it == letter) result++ }

    val result = text.filter { it == 'a' }.length

    println("Number of occurrences of letter $letter in \"$text\" is equal $result")
}

// Napisz funkcję weryfikującą czy dany rok jest przestępny
// https://docs.microsoft.com/en-us/office/troubleshoot/excel/determine-a-leap-year

fun isLeap(year: Int) = when (year % 4) {
    0 -> if (year % 100 == 0) year % 400 == 0 else true
    else -> false
}

// Napisz funkcję sprawdzająca, czy dany znak to litera (duża lub mala ASCII) lub cyfra z wykorzystaniem range

fun isAlphaNumeric(char: Char) = char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9'

// Napisz funkcję obliczający wartość silni dla zadanej liczby całkowitej wykorzystując pętle

fun factorial(value: Long): Long {
    var factorial = 1L
    for (number in 1..value) {
        factorial *= number
    }
    return factorial
}

// Napisz program działający jak prosty kalkulator konsolowy
fun calc() {
    var result = 0.0
    while (true) {
        printMenu()
        val text = readln()
        when (text) {
            "0" -> result = 0.0
            "1" -> result += readln().toDouble()
            "2" -> result -= readln().toDouble()
            "3" -> result *= readln().toDouble()
            "4" -> result /= readln().toDouble()
            else -> break
        }
        println("Result: $result")
    }
}

fun printMenu() {
    println("0 - Reset")
    println("1 - Plus")
    println("2 - Minus")
    println("3 - Multiply")
    println("4 - Divide")
}

/*
   Napisz klasę reprezentującą pieniądze
   Założenia:
   Pieniądze mogą występować w różnych walutach
   Pieniądze mogą być wymieniane / konwertowane do innej waluty po wskazanym kursie
   Pieniądze można ze sobą porównywać, dodawać, odejmować
 */

