package pl.training.payments.application.input

import pl.training.payments.domain.CardNumber
import pl.training.payments.domain.CardTransaction
import pl.training.payments.domain.Money

interface Payments {

    fun chargeCard(number: CardNumber, amount: Money)
    fun chargeCardFees(number: CardNumber)

    fun getCardTransactions(number: CardNumber): List<CardTransaction>

}
