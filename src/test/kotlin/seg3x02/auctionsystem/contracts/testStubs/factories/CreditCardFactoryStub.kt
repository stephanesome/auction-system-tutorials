package seg3x02.auctionsystem.contracts.testStubs.factories

import seg3x02.auctionsystem.application.dtos.queries.AddressCreateDto
import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.domain.user.entities.creditCard.Address
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.factories.CreditCardFactory

class CreditCardFactoryStub: CreditCardFactory {
    override fun createCreditCard(creditCardInfo: CreditCardCreateDto): CreditCard {
        val address = createAddress(creditCardInfo.accountAddress)
        return CreditCard(
            creditCardInfo.number,
            creditCardInfo.expirationMonth,
            creditCardInfo.expirationYear,
            creditCardInfo.accountFirstname,
            creditCardInfo.accountLastname,
            address
        )
    }

    private fun createAddress(addressInfo: AddressCreateDto): Address {
        return Address(
            addressInfo.street,
            addressInfo.city,
            addressInfo.country,
            addressInfo.postalCode
        )
    }
}
