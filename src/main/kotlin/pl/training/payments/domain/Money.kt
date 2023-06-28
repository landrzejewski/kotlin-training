package pl.training.payments.domain

import java.math.BigDecimal
import java.util.*

class Money(amount: BigDecimal, currency: Currency) {

    val amount: BigDecimal
    val currency: Currency

    init {
        this.currency = currency
        this.amount = amount
        require(amount.compareTo(BigDecimal.ZERO) >= 1)
    }

    fun add(money: Money): Money {
        checkCurrencyCompatibility(money)
        return Money(amount.add(money.amount), currency)
    }

    fun subtract(money: Money?): Money {
        checkCurrencyCompatibility(money)
        return Money(amount.subtract(money!!.amount), currency)
    }

    fun multiplyBy(value: Int): Money {
        return Money(amount.multiply(BigDecimal.valueOf(value.toLong())), currency)
    }

    fun isGreaterOrEqual(money: Money?): Boolean {
        checkCurrencyCompatibility(money)
        return amount.compareTo(money!!.amount) > -1
    }

    private fun checkCurrencyCompatibility(money: Money?) {
        require(currency == money!!.currency)
    }

    companion object {

        private val DEFAULT_CURRENCY: Currency = Currency.getInstance("PLN")

        fun of(value: BigDecimal): Money {
            return Money(value, DEFAULT_CURRENCY)
        }

        fun of(value: BigDecimal, currency: Currency): Money {
            return Money(value, currency)
        }

    }

}
