package seg3x02.auctionsystem.domain.user.facade.implementation

import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.auction.events.NewAuctionBidRegistered
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.events.CreditCardCreated
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import seg3x02.auctionsystem.domain.user.factories.CreditCardFactory
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import java.util.*

class UserFacadeImpl(
    private val accountRepository: AccountRepository,
    private val creditCardRepository: CreditCardRepository,
    private var creditCardFactory: CreditCardFactory,
    private var eventEmitter: DomainEventEmitter,
    private var creditService: CreditService): UserFacade {

    override fun addCreditCard(userId: String, creditCardInfo: CreditCardCreateDto) {
        val creditCard = createCreditCard(creditCardInfo)
        val user = accountRepository.find(userId)
        user?.setCreditCard(creditCard, eventEmitter, creditService)
    }

    private fun createCreditCard(creditCardInfo: CreditCardCreateDto): CreditCard {
        val creditCard = creditCardFactory.createCreditCard(creditCardInfo)
        creditCardRepository.save(creditCard)
        eventEmitter.emit(CreditCardCreated(UUID.randomUUID(), Date(), creditCard.number))
        return creditCard
    }

    override fun hasPendingPayment(userId: String): Boolean {
        val user = accountRepository.find(userId)
        return if (user == null) false else user.pendingPayment != null
    }

    override fun getCreditCardNumber(userId: String): String? {
        return accountRepository.find(userId)?.creditCardNumber
    }

    override fun addAuctionToSeller(userId: String, auctionId: UUID) {
        val user = accountRepository.find(userId)
        if (user != null) {
            user.auctions.add(auctionId)
            accountRepository.save(user)
        }
    }

    override fun getPendingPayment(userId: String): PendingPayment? {
        val user = accountRepository.find(userId)
        return user?.pendingPayment
    }

    override fun addBidToAccount(userId: String, bidId: UUID) {
        val user = accountRepository.find(userId)
        if (user != null) {
            user.addBid(bidId)
            val addBidEvent = NewAuctionBidRegistered(
                UUID.randomUUID(),
                Date(),
                bidId,
                userId)
            eventEmitter.emit(addBidEvent)
        }
    }

}
