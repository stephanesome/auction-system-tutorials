package seg3x02.auctionsystem.domain.auction.facade

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import java.math.BigDecimal
import java.util.*

interface AuctionFacade {
    fun addAuction(auctionInfo: AuctionCreateDto, aucItemId: Any): UUID
    fun setAuctionFee(auctionId: UUID, fee: BigDecimal)
}
