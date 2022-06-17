package seg3x02.auctionsystem.domain.auction.facade

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import java.math.BigDecimal
import java.util.*

interface AuctionFacade {
    fun addAuction(auctionInfo: AuctionCreateDto, aucItemId: UUID): UUID
    fun setAuctionFee(auctionId: UUID, fee: BigDecimal)
    fun placeBid(auctionId: UUID, bidInfo: BidCreateDto): UUID?
}
