package seg3x02.auctionsystem.domain.user.entities.account

import java.util.*

class UserAccount(
    val id: String,
    var firstname: String,
    var lastname: String,
    var password: String,
    var email: String) {
    var creditCardNumber: String? = null
    lateinit var role: UserRole
    var pendingPayment: PendingPayment? = null
    val auctions: MutableList<UUID> = ArrayList()
    var bids: MutableList<UUID> = ArrayList()
    var active: Boolean = true
}
