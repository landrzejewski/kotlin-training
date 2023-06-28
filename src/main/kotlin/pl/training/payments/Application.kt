package pl.training.payments

import pl.training.payments.PaymentsFactory.cardRepository
import pl.training.payments.PaymentsFactory.paymentsViewModel
import pl.training.payments.domain.Card
import pl.training.payments.domain.CardId
import pl.training.payments.domain.CardNumber
import pl.training.payments.domain.Money
import pl.training.payments.domain.Money.Companion.DEFAULT_CURRENCY
import java.math.BigDecimal
import java.time.LocalDate

fun main() {
    val cardId = CardId(1)
    val cardNumber = CardNumber("12345678901234567")
    val cardExpirationDate = LocalDate.now().plusYears(1)
    val cardBalance = Money(BigDecimal.valueOf(1_000), DEFAULT_CURRENCY)
    val card = Card(cardId, cardNumber, cardExpirationDate, cardBalance)
    cardRepository.save(card)

    val viewModel = paymentsViewModel()
    viewModel.charge("100")
    viewModel.chargeFees()

    viewModel.getTransactions()
        .forEach { println(it) }
}
