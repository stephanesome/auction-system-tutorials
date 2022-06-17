package seg3x02.auctionsystem.domain.user.events

import seg3x02.auctionsystem.domain.common.DomainEvent
import java.util.*

class UserCreditCardSet(
    val id: UUID,
    val occuredOn: Date,
    val creditCardNumber: String,
    val userId: String): DomainEvent {
}
