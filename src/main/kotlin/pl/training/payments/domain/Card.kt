package pl.training.payments.domain

import pl.training.payments.domain.CardTransactionType.FEE
import pl.training.payments.domain.CardTransactionType.WITHDRAW
import pl.training.payments.domain.Money.Companion.DEFAULT_CURRENCY
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import java.util.function.Consumer

data class Card(
    val id: CardId,
    val number: CardNumber,
    val expiration: LocalDate,
    var balance: Money = Money(ZERO, DEFAULT_CURRENCY),
    val transactions: MutableList<CardTransaction> = mutableListOf()
) {

    private val eventListeners = mutableListOf<Consumer<CardCharged>>()

    fun addTransaction(transaction: CardTransaction) {
        if (!isCardNotExpired(transaction.localDate, expiration)) {
            throw CardExpiredException()
        }
        if (transaction.isWithdraw() && !hasSufficientFunds(balance, transaction)) {
            throw InsufficientFundsException()
        }
        balance = when (transaction.type) {
            FEE, WITHDRAW -> balance.subtract(transaction.money)
        }
        transactions.add(transaction)
        publishChargeEvent(transaction)
    }

    private fun publishChargeEvent(transaction: CardTransaction) {
        val event = CardCharged(number, transaction)
        eventListeners.forEach { it.accept(event) }
    }

    fun addEventsListener(listener: Consumer<CardCharged>) = eventListeners.add(listener)

}
