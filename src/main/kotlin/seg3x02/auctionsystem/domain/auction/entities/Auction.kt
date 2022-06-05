package seg3x02.auctionsystem.domain.auction.entities

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class Auction(
    val id: UUID,
    val startTime: LocalDateTime,
    val duration: Duration,
    val startPrice: BigDecimal,
    val minIncrement: BigDecimal,
    val seller: String,
    val category: AuctionCategory,
    var isclosed: Boolean
) {
    lateinit var item: UUID
    lateinit var fee: BigDecimal
    val bids: MutableList<UUID> = ArrayList()
}

