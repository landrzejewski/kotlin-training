package pl.training.payments.infrastructure.output

import pl.training.payments.application.output.TimeProvider
import java.time.ZonedDateTime

class SystemTimeProvider : TimeProvider {

    override fun getTimestamp() = ZonedDateTime.now()
}
