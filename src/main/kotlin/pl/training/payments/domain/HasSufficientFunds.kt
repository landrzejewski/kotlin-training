package pl.training.payments.domain

fun hasSufficientFunds(balance: Money, transaction: CardTransaction) = balance.isGreaterOrEqual(transaction.money)
