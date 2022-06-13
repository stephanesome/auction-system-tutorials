package seg3x02.auctionsystem.application.dtos.queries

data class AccountCreateDto(
    val userName: String,
    val firstname: String,
    val lastname: String,
    val email: String
)  {
    var creditCardInfo: CreditCardCreateDto? = null
}
