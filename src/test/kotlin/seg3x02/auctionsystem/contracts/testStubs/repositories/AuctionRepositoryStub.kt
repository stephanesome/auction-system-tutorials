package seg3x02.auctionsystem.contracts.testStubs.repositories

import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import java.util.*
import kotlin.collections.HashMap

class AuctionRepositoryStub : AuctionRepository {
    private val auctions: MutableMap<UUID, Auction> = HashMap()

    override fun save(auction: Auction): Auction {
        auctions[auction.id] = auction
        return auction
    }

    override fun find(id: UUID): Auction? = auctions[id]
}
