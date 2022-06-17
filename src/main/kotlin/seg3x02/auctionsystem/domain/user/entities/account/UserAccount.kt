package seg3x02.auctionsystem.domain.user.entities.account

import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.events.UserCreditCardSet
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

    fun setCreditCard(
        creditCard: CreditCard,
        eventEmitter: DomainEventEmitter,
        creditService: CreditService
    ) {
        this.creditCardNumber = creditCard.number
        val ccEvent = UserCreditCardSet(
            id = UUID.randomUUID(),
            occuredOn = Date(),
            creditCardNumber = creditCard.number,
            userId = this.id
        )
        eventEmitter.emit(ccEvent)
        val pending = this.pendingPayment
        if (pending != null) {
            if (creditService.processPayment(
                    creditCard.number,
                    creditCard.expirationMonth,
                    creditCard.expirationYear,
                    pending.amount)) {
                removePendingPayment()
            }
        }
    }

    fun removePendingPayment() {
        pendingPayment = null
    }
}
