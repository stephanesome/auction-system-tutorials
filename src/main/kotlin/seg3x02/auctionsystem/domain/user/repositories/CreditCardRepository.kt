package seg3x02.auctionsystem.domain.user.repositories

import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard

interface CreditCardRepository {
    fun save(creditCard: CreditCard): CreditCard
    fun find(ccNumber: String): CreditCard?
}
