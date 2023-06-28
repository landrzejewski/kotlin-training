package pl.training.payments.infrastructure

import pl.training.payments.application.CardChargedApplicationEvent
import pl.training.payments.application.output.CardEventsPublisher

class ConsoleCardEventsPublisher : CardEventsPublisher {

    override fun publish(event: CardChargedApplicationEvent) = println("New card charge: ${event.number}")

}
