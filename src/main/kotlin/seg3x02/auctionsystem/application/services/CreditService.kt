package seg3x02.auctionsystem.application.services

import java.math.BigDecimal
import java.time.Month
import java.time.Year

interface CreditService {
    fun processPayment(number: String, expirationMonth: Month, expirationYear: Year, amt: BigDecimal): Boolean
}
