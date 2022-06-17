package seg3x02.auctionsystem.domain.item.events

import seg3x02.auctionsystem.domain.common.DomainEvent
import java.util.*

class NewItemAdded(val id: UUID,
                   val occuredOn: Date,
                   val itemId: UUID): DomainEvent {
}
