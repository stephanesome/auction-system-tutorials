package seg3x02.auctionsystem.domain.user.facade.implementation

import seg3x02.auctionsystem.application.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.auction.events.NewAuctionBidRegistered
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.events.CreditCardCreated
import seg3x02.auctionsystem.domain.user.events.UserAccountCreated
import seg3x02.auctionsystem.domain.user.events.UserAccountDeactivated
import seg3x02.auctionsystem.domain.user.events.UserAccountUpdated
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import seg3x02.auctionsystem.domain.user.factories.AccountFactory
import seg3x02.auctionsystem.domain.user.factories.CreditCardFactory
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import java.math.BigDecimal
import java.util.*

class UserFacadeImpl(
    private val accountRepository: AccountRepository,
    private val accountFactory: AccountFactory,
    private val creditCardRepository: CreditCardRepository,
    private var creditCardFactory: CreditCardFactory,
    private var eventEmitter: DomainEventEmitter,
    private var creditService: CreditService): UserFacade {

    override fun addCreditCard(userId: String, creditCardInfo: CreditCardCreateDto) {
        val creditCard = createCreditCard(creditCardInfo)
        val user = accountRepository.find(userId)
        user?.setCreditCard(creditCard, eventEmitter, creditService)
    }

    override fun createAccount(accountInfo: AccountCreateDto): Boolean {
        val userId = accountInfo.userName
        val existAccount = accountRepository.find(userId)
        if (existAccount != null) {
            return false
        }
        val userAccount = accountFactory.createAccount(accountInfo)
        val ccInfo = accountInfo.creditCardInfo
        if (ccInfo != null) {
            val ccCard = createCreditCard(ccInfo)
            userAccount.setCreditCard(ccCard, eventEmitter, creditService)
        }
        accountRepository.save(userAccount)
        eventEmitter.emit(
            UserAccountCreated(UUID.randomUUID(),
                Date(),
                userAccount.id)
        )
        return true
    }

    override fun updateAccount(userId: String, accountInfo: AccountCreateDto): Boolean {
        val user = accountRepository.find(userId)
        if (user != null) {
            val updated = accountFactory.createAccount(accountInfo)
            user.update(updated)
            val ccInfo = accountInfo.creditCardInfo
            if (ccInfo != null) {
                val newCard: CreditCard = createCreditCard(ccInfo)
                user.setCreditCard(newCard, eventEmitter, creditService)
            }
            accountRepository.save(user)
            eventEmitter.emit(
                UserAccountUpdated(UUID.randomUUID(),
                    Date(),
                    user.id)
            )
            return true
        }
        return false
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

    override fun getUserEmailAddress(userId: String): String? {
        return accountRepository.find(userId)?.email
    }

    override fun getUserCreditCard(userId: String): CreditCard? {
        val ccNumber = this.getCreditCardNumber(userId)
        return if (ccNumber != null) {
            creditCardRepository.find(ccNumber)
        } else null
    }

    override fun addPendingPayment(userId: String, amt: BigDecimal) {
        val user = accountRepository.find(userId)
        user?.addPendingPayment(amt)
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

    override fun deactivateAccount(userId: String): Boolean {
        val user = accountRepository.find(userId)
        if (user != null) {
            user.deactivate()
            eventEmitter.emit(
                UserAccountDeactivated(
                UUID.randomUUID(),
                Date(),
                user.id
            )
            )
            return true
        }
        return false
    }

    override fun getUserAuctions(userId: String): List<UUID> {
        val auctionList = ArrayList<UUID>()
        val user = accountRepository.find(userId)
        if (user != null) {
            auctionList.addAll(user.auctions)
        }
        return auctionList
    }

}
