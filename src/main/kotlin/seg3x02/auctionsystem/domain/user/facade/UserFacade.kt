package seg3x02.auctionsystem.domain.user.facade

import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import java.util.*

interface UserFacade {
    fun addCreditCard(userId: String, creditCardInfo: CreditCardCreateDto)
    fun hasPendingPayment(userId: String): Boolean
    fun getCreditCardNumber(userId: String): String?
    fun addAuctionToSeller(userId: String, auctionId: UUID)
    fun getPendingPayment(userId: String): PendingPayment?
    fun addBidToAccount(userId: String, bidId: UUID)
}
