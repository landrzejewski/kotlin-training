package pl.training.payments.application.output

import java.time.ZonedDateTime

interface TimeProvider {

    fun getTimestamp(): ZonedDateTime

}
