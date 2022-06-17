package seg3x02.auctionsystem.contracts.testStubs.services

import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.common.DomainEvent
import seg3x02.auctionsystem.domain.item.events.NewItemAdded
import seg3x02.auctionsystem.domain.user.events.CreditCardCreated
import kotlin.collections.ArrayList

class EventEmitterStub : DomainEventEmitter {
    private val emitted: MutableList<DomainEvent> = ArrayList()

    override fun emit(event: DomainEvent) {
        emitted.add(event)
    }

    fun retrieveNewItemAddedEvent(): NewItemAdded {
        return emitted.find { it is NewItemAdded} as NewItemAdded

    }

    fun retrieveCreditCardCreatedEvent(): CreditCardCreated {
        return emitted.find { it is CreditCardCreated} as CreditCardCreated
    }

}
