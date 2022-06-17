package seg3x02.auctionsystem.contracts.steps

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime

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
