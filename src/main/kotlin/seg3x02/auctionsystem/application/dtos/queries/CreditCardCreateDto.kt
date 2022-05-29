package seg3x02.auctionsystem.application.dtos.queries

import java.time.Month
import java.time.Year

data class CreditCardCreateDto(
    val number: String,
    val expirationMonth: Month,
    val expirationYear: Year,
    val accountFirstname: String,
    val accountLastname: String,
    val accountAddress: AddressCreateDto)
