package pl.training.payments.domain

data class CardCharged(val number: CardNumber, val transaction: CardTransaction)
