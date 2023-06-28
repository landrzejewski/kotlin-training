package pl.training.payments

import pl.training.payments.application.PaymentsService
import pl.training.payments.application.input.Payments
import pl.training.payments.application.output.CardEventsPublisher
import pl.training.payments.application.output.CardRepository
import pl.training.payments.application.output.TimeProvider
import pl.training.payments.infrastructure.input.ViewModel
import pl.training.payments.infrastructure.output.ConsoleCardEventsPublisher
import pl.training.payments.infrastructure.output.InMemoryCardRepository
import pl.training.payments.infrastructure.output.SystemTimeProvider

object PaymentsFactory {

    val cardRepository: CardRepository = InMemoryCardRepository()

    private fun timeProvider(): TimeProvider = SystemTimeProvider()

    private val cardEventsPublisher: CardEventsPublisher = ConsoleCardEventsPublisher()

    private fun payments(): Payments = PaymentsService(cardRepository, timeProvider(), cardEventsPublisher)

    fun paymentsViewModel() = ViewModel(payments())

}
