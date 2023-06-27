package pl.training

import java.lang.IllegalArgumentException

class Calculator {

    fun add(firstNumber: Double, secondNumber: Double) = firstNumber + secondNumber

    fun divide(firstNumber: Double, secondNumber: Double): Double {
        if (secondNumber == 0.0) {
            throw IllegalArgumentException()
        }
        return firstNumber / secondNumber
    }

}
