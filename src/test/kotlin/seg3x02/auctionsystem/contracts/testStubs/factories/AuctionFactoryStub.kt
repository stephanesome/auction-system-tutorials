package seg3x02.auctionsystem.contracts.testStubs.factories

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.domain.auction.factories.AuctionFactory
import java.util.*

class AuctionFactoryStub: AuctionFactory {
    override fun createAuction(auctionInfo: AuctionCreateDto, aucItemId: UUID): Auction {
        var categ = AuctionCategory(auctionInfo.category)
        var auction = Auction(
            UUID.randomUUID(),
            auctionInfo.startTime,
            auctionInfo.duration,
            auctionInfo.startPrice,
            auctionInfo.minIncrement,
            auctionInfo.seller,
            categ,
            false
        )
        auction.item = aucItemId
        return auction
    }
}
