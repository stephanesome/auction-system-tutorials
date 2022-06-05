package seg3x02.auctionsystem.domain.item.entities

import java.util.*

class Item(val id: UUID,
        val title: String,
        val description: String) {
    var image: ByteArray = ByteArray(0)
}
