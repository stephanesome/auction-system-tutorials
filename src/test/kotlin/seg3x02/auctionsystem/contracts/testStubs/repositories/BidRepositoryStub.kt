package seg3x02.auctionsystem.contracts.testStubs.repositories

import seg3x02.auctionsystem.domain.auction.entities.Bid
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import java.util.*

class BidRepositoryStub: BidRepository {
    private val bids: MutableMap<UUID, Bid> = HashMap()

    override fun find(id: UUID): Bid? = bids[id]

    override fun save(bid: Bid): Bid {
        bids[bid.id] = bid
        return bid
    }
}
