package seg3x02.auctionsystem.domain.auction.factories

import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.domain.auction.entities.Bid

interface BidFactory {
    fun createBid(bidInfo: BidCreateDto): Bid
}
