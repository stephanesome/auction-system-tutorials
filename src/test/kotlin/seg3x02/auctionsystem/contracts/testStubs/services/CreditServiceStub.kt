package seg3x02.auctionsystem.contracts.testStubs.services

import seg3x02.auctionsystem.application.services.CreditService
import java.math.BigDecimal
import java.time.Month
import java.time.Year

class CreditServiceStub : CreditService {
    private var returnVal: Boolean = true
    override fun processPayment(
        number: String,
        expirationMonth: Month,
        expirationYear: Year,
        amt: BigDecimal
    ): Boolean {
        return returnVal
    }

    fun setReturnVal(returnVal: Boolean) {
        this.returnVal = returnVal
    }
}
