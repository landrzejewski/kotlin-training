package pl.training.payments.domain

import pl.training.payments.domain.Money.Companion.DEFAULT_CURRENCY
import java.math.BigDecimal.ONE

class CardTransactionBasedFees(val transactions: List<CardTransaction>) : Policy<Money> {

    override fun execute() = singleTransactionFee.multiplyBy(transactions.size)

    companion object {

        val singleTransactionFee = Money(ONE, DEFAULT_CURRENCY)

    }

}
