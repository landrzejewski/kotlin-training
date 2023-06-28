package pl.training.payments.infrastructure.input

import pl.training.payments.application.input.Payments
import pl.training.payments.domain.CardNumber
import pl.training.payments.domain.Money
import pl.training.payments.domain.Money.Companion.DEFAULT_CURRENCY
import java.math.BigDecimal

class ViewModel(val payments: Payments) {

    private val cardNumber = "12345678901234567"

    fun charge(chargeValue: String) {
        val number = CardNumber(cardNumber)
        val amount = Money(BigDecimal(chargeValue), DEFAULT_CURRENCY)
        payments.chargeCard(number, amount)
    }

    fun getTransactions() = payments.getCardTransactions(CardNumber(cardNumber))

    fun chargeFees() = payments.chargeCardFees(CardNumber(cardNumber))

}
