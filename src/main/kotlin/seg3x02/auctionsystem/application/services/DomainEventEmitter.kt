package seg3x02.auctionsystem.application.services

import seg3x02.auctionsystem.domain.common.DomainEvent

interface DomainEventEmitter {
    fun emit(event: DomainEvent)
}
