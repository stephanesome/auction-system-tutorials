package seg3x02.auctionsystem.application.usecases

import java.util.*

interface CloseAuction {
    fun closeAuction(auctionId: UUID): String?
}
