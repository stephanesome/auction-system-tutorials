package seg3x02.auctionsystem.domain.user.events

import seg3x02.auctionsystem.domain.common.DomainEvent
import java.util.*

class UserAccountUpdated(val id: UUID,
                         val occuredOn: Date,
                         val userId: String): DomainEvent {
}
