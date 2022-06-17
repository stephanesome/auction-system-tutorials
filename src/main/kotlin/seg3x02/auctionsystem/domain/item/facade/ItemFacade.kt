package seg3x02.auctionsystem.domain.item.facade

import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import java.util.*

interface ItemFacade {
    fun addItem(itemInfo: ItemCreateDto): UUID
}
