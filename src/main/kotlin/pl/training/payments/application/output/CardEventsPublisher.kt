package pl.training.payments.application.output

import pl.training.payments.application.CardChargedApplicationEvent

fun interface CardEventsPublisher {

    fun publish(event: CardChargedApplicationEvent)

}
