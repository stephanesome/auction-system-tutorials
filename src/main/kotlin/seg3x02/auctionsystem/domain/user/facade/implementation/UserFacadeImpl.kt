package seg3x02.auctionsystem.domain.user.facade.implementation

import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.application.services.DomainEventEmitter
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
    private var eventEmitter: DomainEventEmitter): UserFacade {

    override fun addCreditCard(userId: String, creditCardInfo: CreditCardCreateDto) {
        val creditCard = creditCardFactory.createCreditCard(creditCardInfo)
        creditCardRepository.save(creditCard)
        eventEmitter.emit(CreditCardCreated(UUID.randomUUID(), Date(), creditCard.number))
        val user = accountRepository.find(userId)
        user?.setCreditCard(creditCard)
    }

    override fun hasPendingPayment(seller: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCreditCardNumber(seller: String): String? {
        TODO("Not yet implemented")
    }

    override fun addAuctionToSeller(seller: String, auctionId: UUID) {
        TODO("Not yet implemented")
    }

}
