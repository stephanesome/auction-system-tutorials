package seg3x02.auctionsystem.contracts.testStubs.repositories

import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.repositories.ItemRepository
import java.util.*
import kotlin.collections.HashMap

class ItemRepositoryStub : ItemRepository {
    private val items: MutableMap<UUID, Item> = HashMap()

    override fun save(item: Item): Item {
        items[item.id] = item
        return item
    }

    override fun find(id: UUID): Item? = items[id]
}
