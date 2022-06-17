package seg3x02.auctionsystem.domain.item.factories

import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.domain.item.entities.Item

interface ItemFactory {
    fun createItem(itemInfo: ItemCreateDto): Item
}
