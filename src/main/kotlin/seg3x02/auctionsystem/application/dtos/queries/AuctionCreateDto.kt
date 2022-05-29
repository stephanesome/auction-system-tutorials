package seg3x02.auctionsystem.application.dtos.queries

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime

data class AuctionCreateDto(val startTime: LocalDateTime,
                       val duration: Duration,
                       val startPrice: BigDecimal,
                       val minIncrement: BigDecimal,
                       val seller: String,
                       val category: String,
                            var itemInfo: ItemCreateDto) {
    var creditCardInfo: CreditCardCreateDto? = null
}
