package pl.training.payments.application.output

import pl.training.payments.domain.Card
import pl.training.payments.domain.CardNumber

interface CardRepository {

    fun getByNumber(cardNumber: CardNumber): Card?

    fun save(card: Card)

}
