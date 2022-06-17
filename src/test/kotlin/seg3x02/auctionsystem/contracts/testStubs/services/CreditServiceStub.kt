package seg3x02.auctionsystem.contracts.testStubs.services

import seg3x02.auctionsystem.application.services.CreditService
import java.math.BigDecimal
import java.time.Month
import java.time.Year

class CreditServiceStub : CreditService {
    override fun processPayment(
        number: String,
        expirationMonth: Month,
        expirationYear: Year,
        amt: BigDecimal
    ): Boolean {
        return true
    }

}
