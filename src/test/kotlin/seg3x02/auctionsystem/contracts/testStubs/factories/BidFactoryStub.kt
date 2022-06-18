package seg3x02.auctionsystem.contracts.testStubs.factories

import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.domain.auction.entities.Bid
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import java.util.*

class BidFactoryStub: BidFactory {
    override fun createBid(bidInfo: BidCreateDto): Bid {
        return Bid(
            UUID.randomUUID(),
            bidInfo.amount,
            bidInfo.date,
            bidInfo.buyer
        )
    }
}
