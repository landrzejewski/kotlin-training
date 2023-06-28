package pl.training.payments.domain

import java.math.BigDecimal
import java.util.*

class Money(val amount: BigDecimal, val currency: Currency) {

    init {
        require(amount.compareTo(BigDecimal.ZERO) >= 1)
    }

    fun add(money: Money): Money {
        checkCurrencyCompatibility(money)
        return Money(amount.add(money.amount), currency)
    }

    fun subtract(money: Money): Money {
        checkCurrencyCompatibility(money)
        return Money(amount.subtract(money.amount), currency)
    }

    fun multiplyBy(value: Int) = Money(amount.multiply(BigDecimal.valueOf(value.toLong())), currency)

    fun isGreaterOrEqual(money: Money): Boolean {
        checkCurrencyCompatibility(money)
        return amount.compareTo(money.amount) > -1
    }

    private fun checkCurrencyCompatibility(money: Money) = require(currency == money.currency)

    operator fun plus(money: Money) = add(money)

    operator fun minus(money: Money) = subtract(money)

    fun convert(rate: BigDecimal, expectedCurrency: Currency) = Money(amount.multiply(rate), expectedCurrency)

    companion object {

        private val DEFAULT_CURRENCY: Currency = Currency.getInstance("PLN")

        fun of(value: BigDecimal) = Money(value, DEFAULT_CURRENCY)

        fun of(value: BigDecimal, currency: Currency) = Money(value, currency)

    }

}
