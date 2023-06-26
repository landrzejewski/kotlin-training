package pl.training

data class Money(val value: Double, val currency: String) {

    fun add(money: Money): Money {
        checkCurrency(money)
        return copy(value = value + money.value)
    }

    fun subtract(money: Money): Money {
        checkCurrency(money)
        return copy(value = value - money.value)
    }

    private fun checkCurrency(money: Money) {
        if (currency != money.currency) {
            throw IllegalArgumentException()
        }
    }

    operator fun plus(money: Money) = add(money)

    operator fun minus(money: Money) = subtract(money)

    fun convert(rate: Double, expectedCurrency: String) = Money(value * rate, expectedCurrency)

}
