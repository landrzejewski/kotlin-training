package pl.training.payments.application

import pl.training.payments.application.input.Payments
import pl.training.payments.application.output.CardEventsPublisher
import pl.training.payments.application.output.CardRepository
import pl.training.payments.application.output.TimeProvider
import pl.training.payments.domain.*
import pl.training.payments.domain.CardTransactionType.FEE
import java.util.function.Consumer

data class PaymentsService(
    private val repository: CardRepository,
    private val timeProvider: TimeProvider,
    private val eventPublisher: CardEventsPublisher
) : Payments {

    override fun chargeCard(number: CardNumber, amount: Money) = processOperation(number) {
        it.addEventsListener(createEventListener())
        CardTransaction(timeProvider.getTimestamp(), amount)
    }

    override fun chargeCardFees(number: CardNumber) = processOperation(number) {
        val fees = CardTransactionBasedFees(it.transactions).execute()
        CardTransaction(timeProvider.getTimestamp(), fees, FEE)
    }

    override fun getCardTransactions(number: CardNumber) = getCard(number).transactions

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
