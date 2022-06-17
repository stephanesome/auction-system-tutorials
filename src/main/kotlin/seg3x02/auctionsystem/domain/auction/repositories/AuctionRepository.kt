package seg3x02.auctionsystem.domain.auction.repositories

import seg3x02.auctionsystem.domain.auction.entities.Auction
import java.util.*

interface AuctionRepository {
    fun save(auction: Auction): Auction
    fun find(id: UUID): Auction?
}
