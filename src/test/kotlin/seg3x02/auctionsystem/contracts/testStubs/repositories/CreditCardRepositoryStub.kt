package seg3x02.auctionsystem.contracts.testStubs.repositories

import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import kotlin.collections.HashMap

class CreditCardRepositoryStub : CreditCardRepository {
    private val cCards: MutableMap<String, CreditCard> = HashMap()

    override fun save(creditCard: CreditCard): CreditCard {
        cCards[creditCard.number] = creditCard
        return creditCard
    }

    override fun find(ccNumber: String): CreditCard? = cCards[ccNumber]
}
