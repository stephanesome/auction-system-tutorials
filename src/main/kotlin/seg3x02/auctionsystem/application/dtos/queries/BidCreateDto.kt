package seg3x02.auctionsystem.application.dtos.queries

import java.math.BigDecimal
import java.time.LocalDateTime

data class BidCreateDto(val amount: BigDecimal,
                   val date: LocalDateTime,
                   val buyer: String) {
}
