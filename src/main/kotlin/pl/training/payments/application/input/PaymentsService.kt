package pl.training.payments.application.input

import pl.training.payments.domain.CardNumber
import pl.training.payments.domain.Money

interface PaymentsService {

    fun chargeCard(number: CardNumber, amount: Money)
    fun chargeFees(number: CardNumber)

}
