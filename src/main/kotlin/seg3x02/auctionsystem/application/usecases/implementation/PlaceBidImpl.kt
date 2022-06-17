package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.application.usecases.PlaceBid
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import java.math.BigDecimal
import java.util.*

class PlaceBidImpl(
    private var userFacade: UserFacade,
    private var auctionFacade: AuctionFacade): PlaceBid {

    override fun placeBid(accountId: String, auctionId: UUID, bidInfo: BidCreateDto): UUID? {
        val pendingPayment = userFacade.getPendingPayment(accountId)
        if ((pendingPayment == null) ||
            (pendingPayment.amount == BigDecimal(0.0))
        ) {
            val bidId = auctionFacade.placeBid(auctionId, bidInfo)
            if (bidId != null) {
                userFacade.addBidToAccount(accountId, bidId)

                return bidId
            }
        }
        return null
    }
}
