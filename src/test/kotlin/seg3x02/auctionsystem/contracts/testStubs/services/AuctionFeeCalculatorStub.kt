package seg3x02.auctionsystem.contracts.testStubs.services

import seg3x02.auctionsystem.domain.auction.services.AuctionFeeCalculator
import java.math.BigDecimal
import java.util.*

class AuctionFeeCalculatorStub: AuctionFeeCalculator {
    override fun getAuctionFee(auctionId: UUID): BigDecimal {
        return BigDecimal(10.00)
    }
}
