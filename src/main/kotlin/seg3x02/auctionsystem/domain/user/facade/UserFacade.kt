package seg3x02.auctionsystem.domain.user.facade

import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import java.util.*

interface UserFacade {
    fun addCreditCard(seller: String, ccInfo: CreditCardCreateDto)
    fun hasPendingPayment(seller: String): Boolean
    fun getCreditCardNumber(seller: String): String?
    fun addAuctionToSeller(seller: String, auctionId: UUID)
}
