package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.EmailService
import seg3x02.auctionsystem.application.usecases.CloseAuction
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import java.math.BigDecimal
import java.util.*

class CloseAuctionImpl(
    private var userFacade: UserFacade,
    private var auctionFacade: AuctionFacade,
    private var creditService: CreditService,
    private var emailService: EmailService): CloseAuction {

    override fun closeAuction(auctionId: UUID): String? {
        val winnerId = auctionFacade.closeAuction(auctionId)
        if (winnerId != null) {
            val amt = auctionFacade.getAuctionFee(auctionId)
            val sellerId = auctionFacade.getAuctionSeller(auctionId)
            if (sellerId != null && amt != null) {
                val sellerEmailAddress = userFacade.getUserEmailAddress(sellerId)
                val creditCard = userFacade.getUserCreditCard(sellerId)
                if (creditCard != null) {
                    val creditResult = creditService.processPayment(
                        creditCard.number,
                        creditCard.expirationMonth,
                        creditCard.expirationYear,
                        amt
                    )
                    if (!creditResult) {
                        userFacade.addPendingPayment(sellerId, amt)
                        if (sellerEmailAddress != null) {
                            emailService.sendSellerCompletedAuctionPaymentProcessFail(sellerEmailAddress)
                        }
                    } else {
                        if (sellerEmailAddress != null) {
                            emailService.sendSellerCompletedAuctionPaymentProcessedFine(sellerEmailAddress)
                        }
                    }
                    val winnerEmailAddress = userFacade.getUserEmailAddress(winnerId)
                    if (winnerEmailAddress != null) {
                        emailService.sendWinnerCompletedAuctionEmailWithWinningBid(winnerEmailAddress)
                    }
                }
            }
        } else {
            // no winner
            val sellerId = auctionFacade.getAuctionSeller(auctionId)
            if (sellerId != null) {
                val sellerEmailAddress = userFacade.getUserEmailAddress(sellerId)
                if (sellerEmailAddress != null) {
                    emailService.sendSellerCompletedAuctionEmailNoWinningBid(sellerEmailAddress)
                }
            }
        }
        return winnerId
    }
}
