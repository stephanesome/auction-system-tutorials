package seg3x02.auctionsystem.domain.auction.events

import seg3x02.auctionsystem.domain.common.DomainEvent
import java.util.*

class AuctionClosed(val id: UUID,
                    val occuredOn: Date,
                    val auctionId: UUID,
                    val winnerId: UUID?): DomainEvent
