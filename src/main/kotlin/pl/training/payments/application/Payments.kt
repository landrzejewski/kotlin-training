package pl.training.payments.application

import pl.training.payments.application.output.CardEventsPublisher
import pl.training.payments.application.output.CardRepository
import pl.training.payments.application.output.TimeProvider
import pl.training.payments.domain.*
import pl.training.payments.domain.CardTransactionType.FEE
import java.util.function.Consumer

data class Payments(
    private val repository: CardRepository,
    private val timeProvider: TimeProvider,
    private val eventPublisher: CardEventsPublisher
) {

    fun chargeCard(number: CardNumber, amount: Money) = processOperation(number) {
        it.eventListeners.add(createEventListener())
        CardTransaction(timeProvider.getTimestamp(), amount)
    }

    fun chargeFees(number: CardNumber) = processOperation(number) {
        val fees = CardTransactionBasedFees(it.transactions).execute()
        CardTransaction(timeProvider.getTimestamp(), fees, FEE)
    }

    private fun processOperation(number: CardNumber, operation: (Card) -> CardTransaction) {
        val card = getCard(number)
        val transaction = operation(card)
        card.addTransaction(transaction)
        repository.save(card)
    }

    private fun getCard(number: CardNumber) = repository.getByNumber(number) ?: throw CardNotFoundException()

    private fun createEventListener(): Consumer<CardCharged> {
        return Consumer<CardCharged> {
            val appEvent = CardChargedApplicationEvent(it.number.toString())
            eventPublisher.publish(appEvent)
        }
    }

}
