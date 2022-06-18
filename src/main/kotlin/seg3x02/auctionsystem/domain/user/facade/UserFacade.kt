package seg3x02.auctionsystem.domain.user.facade

import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import java.math.BigDecimal
import java.util.*

interface UserFacade {
    fun addCreditCard(userId: String, creditCardInfo: CreditCardCreateDto)
    fun hasPendingPayment(userId: String): Boolean
    fun getCreditCardNumber(userId: String): String?
    fun addAuctionToSeller(userId: String, auctionId: UUID)
    fun getPendingPayment(userId: String): PendingPayment?
    fun getUserEmailAddress(userId: String): String?
    fun getUserCreditCard(userId: String): CreditCard?
    fun addPendingPayment(userId: String, amt: BigDecimal)
    fun addBidToAccount(userId: String, bidId: UUID)
}
