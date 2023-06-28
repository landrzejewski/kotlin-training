package pl.training.payments.domain

import java.time.LocalDate

class CardNotExpired(private val localDate: LocalDate, private val expirationDate: LocalDate) : Specification {

    override fun check() = localDate.isBefore(expirationDate)

}

fun isCardNotExpired(localDate: LocalDate, expirationDate: LocalDate) = CardNotExpired(localDate, expirationDate).check()
