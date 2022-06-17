package seg3x02.auctionsystem.contracts.steps

import seg3x02.auctionsystem.application.dtos.queries.AddressCreateDto
import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year

fun createAccount(accountRepository: AccountRepository): UserAccount {
    val acc = UserAccount("userXXX",
        "Toto",
        "Tata",
        "pass",
        "toto@somewhere.com")
    accountRepository.save(acc)
    return acc
}

fun addCreditCardToAccount(account: UserAccount) {
    account.creditCardNumber = "55555555"
}

fun setItemInfo(): ItemCreateDto {
    return ItemCreateDto("Game boy",
        "Still wrapped in")
}

fun setAuctionInfo(userId: String, itemInfo: ItemCreateDto): AuctionCreateDto {
    return AuctionCreateDto(
        LocalDateTime.now(),
        Duration.ofDays(5),
        BigDecimal(100.00),
        BigDecimal(5.00),
        userId,
        "Toy",
        itemInfo)
}

fun setCreditCardInfo(): CreditCardCreateDto {
    val addr = AddressCreateDto(
        "125 DeLa Rue",
        "Ottawa",
        "Canada",
        "K0K0K0")
    return CreditCardCreateDto("6666666",
        Month.AUGUST,
        Year.parse("2024"),
        "Toto",
        "Tata",
        addr
    )
}
