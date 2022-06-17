package seg3x02.auctionsystem.contracts.testStubs.factories

import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.factories.ItemFactory
import java.util.*

class ItemFactoryStub: ItemFactory {
    override fun createItem(itemInfo: ItemCreateDto): Item {
        return Item(UUID.randomUUID(),
                    itemInfo.title,
                    itemInfo.description)
    }
}
