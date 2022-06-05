package seg3x02.auctionsystem.domain.user.entities.creditCard

import java.time.Month
import java.time.Year

class CreditCard(
    val number: String,
    val expirationMonth: Month,
    val expirationYear: Year,
    val accountFirstname: String,
    val accountLastname: String,
    val accountAddress: Address
) {
}
