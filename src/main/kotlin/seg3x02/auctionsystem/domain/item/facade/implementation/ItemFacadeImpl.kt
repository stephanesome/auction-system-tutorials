package seg3x02.auctionsystem.domain.item.facade.implementation

import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.item.events.NewItemAdded
import seg3x02.auctionsystem.domain.item.facade.ItemFacade
import seg3x02.auctionsystem.domain.item.factories.ItemFactory
import seg3x02.auctionsystem.domain.item.repositories.ItemRepository
import java.util.*

class ItemFacadeImpl(private var itemFactory: ItemFactory,
                     private var itemRepository: ItemRepository,
                     private var eventEmitter: DomainEventEmitter): ItemFacade {

    override fun addItem(itemInfo: ItemCreateDto): UUID {
        val item = itemFactory.createItem(itemInfo)
        itemRepository.save(item)
        eventEmitter.emit(NewItemAdded(UUID.randomUUID(), Date(), item.id))
        return item.id
    }
}
