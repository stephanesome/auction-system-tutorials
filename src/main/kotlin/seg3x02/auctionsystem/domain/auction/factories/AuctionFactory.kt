package seg3x02.auctionsystem.domain.auction.factories

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.domain.auction.entities.Auction
import java.util.*

interface AuctionFactory {
    fun createAuction(auctionInfo: AuctionCreateDto, aucItemId: UUID): Auction
}
