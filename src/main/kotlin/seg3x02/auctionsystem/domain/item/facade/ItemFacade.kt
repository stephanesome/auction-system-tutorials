package seg3x02.auctionsystem.domain.item.facade

import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto

interface ItemFacade {
    fun addItem(itemInfo: ItemCreateDto): Any
}
