package seg3x02.auctionsystem.contracts.testStubs.services

import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.common.DomainEvent
import seg3x02.auctionsystem.domain.item.events.NewItemAdded
import kotlin.collections.ArrayList

class EventEmitterStub : DomainEventEmitter {
    private val emitted: MutableList<DomainEvent> = ArrayList()

    override fun emit(event: DomainEvent) {
        emitted.add(event)
    }

    fun retrieveNewItemAddedEvent(): NewItemAdded? {
        return emitted.find { it is NewItemAdded} as NewItemAdded
    }

/*    fun retrieveNewAuctionAddedEvent(): NewAuctionAdded? {
        return emitted.find { it is NewAuctionAdded} as NewAuctionAdded
    }

    fun retrieveNewBidCreatedEvent(): NewBidCreated? {
        return emitted.find { it is NewBidCreated} as NewBidCreated
    }

    fun retrieveUserAccountCreatedEvent(): UserAccountCreated {
        return emitted.find { it is UserAccountCreated} as UserAccountCreated
    }

    fun retrieveCreditCardCreatedEvent(): CreditCardCreated {
        return emitted.find { it is CreditCardCreated} as CreditCardCreated
    }

    fun retrieveUserCreditCardSetEvent(): UserCreditCardSet {
        return emitted.find { it is UserCreditCardSet} as UserCreditCardSet
    }

    fun retrieveUserAccountUpdatedEvent(): UserAccountUpdated {
        return emitted.find { it is UserAccountUpdated} as UserAccountUpdated
    }

    fun retrieveUserAccountDeactivatedEvent(): UserAccountDeactivated {
        return emitted.find { it is UserAccountDeactivated} as UserAccountDeactivated
    }*/
}
