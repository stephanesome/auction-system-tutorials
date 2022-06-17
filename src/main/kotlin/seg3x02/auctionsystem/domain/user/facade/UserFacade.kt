package seg3x02.auctionsystem.domain.user.facade

import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import java.util.*

interface UserFacade {
    fun addCreditCard(userId: String, creditCardInfo: CreditCardCreateDto)
    fun hasPendingPayment(userId: String): Boolean
    fun getCreditCardNumber(userId: String): String?
    fun addAuctionToSeller(userId: String, auctionId: UUID)
}
