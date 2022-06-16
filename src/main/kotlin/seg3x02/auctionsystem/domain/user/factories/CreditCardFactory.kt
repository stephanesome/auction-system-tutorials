package seg3x02.auctionsystem.domain.user.factories

import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard

interface CreditCardFactory {
    fun createCreditCard(creditCardInfo: CreditCardCreateDto): CreditCard
}
