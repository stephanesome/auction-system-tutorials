package seg3x02.auctionsystem.domain.auction.services

import java.math.BigDecimal
import java.util.*

interface AuctionFeeCalculator {
    fun getAuctionFee(auctionId: UUID): BigDecimal
}
