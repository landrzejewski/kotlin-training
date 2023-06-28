package pl.training.payments.domain

import pl.training.payments.domain.CardTransactionType.WITHDRAW
import java.time.ZonedDateTime

data class CardTransaction(val timestamp: ZonedDateTime, val money: Money, val type: CardTransactionType = WITHDRAW) {

    val localDate = timestamp.toLocalDate()

    fun isWithdraw() = type == WITHDRAW

}
