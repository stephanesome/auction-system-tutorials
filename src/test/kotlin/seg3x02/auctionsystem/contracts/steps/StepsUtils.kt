package seg3x02.auctionsystem.contracts.steps

import seg3x02.auctionsystem.application.dtos.queries.*
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.Address
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.util.*

fun createAccount(accountRepository: AccountRepository): UserAccount {
    val acc = UserAccount("userXXX",
        "Toto",
        "Tata",
        "pass",
        "toto@somewhere.com")
    accountRepository.save(acc)
    return acc
}

fun addCreditCardToAccount(account: UserAccount,
            creditCardRepository: CreditCardRepository) {
    val cc = CreditCard("5555555",
        Month.JUNE,
        Year.parse("2024"),
        "Toto",
        "Tata",
        Address(
            "125 DeLa Rue",
            "Ottawa",
            "Canada",
            "K0K0K0")
    )
    creditCardRepository.save(cc)
    account.creditCardNumber = cc.number
}

fun addPendingPaymentToAccount(account: UserAccount) {
    val pendingAmount = BigDecimal(20)
    account.pendingPayment = PendingPayment(
        pendingAmount
    )
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

fun setCreditCardUpdateInfo(): CreditCardCreateDto {
    val addr = AddressCreateDto(
        "125 DeLa Rue Updated",
        "Ottawa Updated",
        "Canada Updated",
        "K0K0K0 Updated")
    return CreditCardCreateDto("6666666 Updated",
        Month.DECEMBER,
        Year.parse("2027"),
        "Toto Updated",
        "Tata Updated",
        addr
    )
}

fun setAccountInfo(): AccountCreateDto {
    return AccountCreateDto(
        "user77876",
        "password",
        "Toto",
        "Tata",
        "toto@somewhere.com")
}

fun setUpdateAccountInfo(): AccountCreateDto {
    return AccountCreateDto(
        "userUpdated",
        "passwordUpdated",
        "TotoUpdated",
        "TataUpdated",
        "totoUpdated@somewhere.com")
}

fun createAuction(auctionRepository: AuctionRepository): Auction {
    val auctionId = UUID.randomUUID()
    val auction = Auction(auctionId,
        LocalDateTime.now(),
        Duration.ofDays(3),
        BigDecimal(100),
        BigDecimal(5),
        "sellerXXX",
        AuctionCategory("Toy"),
        false
    )
    val itemId = UUID.randomUUID()
    val item = Item(itemId,
        "Toy",
        "Very rare")
    auction.item = itemId
    auctionRepository.save(auction)
    return auction
}

fun createAuctionForSeller(sellerId: String,
                           auctionRepository: AuctionRepository,
                           bidFactory: BidFactory,
                           bidRepository: BidRepository): Auction {
    val auctionId = UUID.randomUUID()
    val auction = Auction(auctionId,
        LocalDateTime.of(2020,1,1,12,0,0),
        Duration.ofDays(3),
        BigDecimal(100),
        BigDecimal(5),
        sellerId,
        AuctionCategory("Toy"),
        false
    )
    auction.fee = BigDecimal(10)
    val itemId = UUID.randomUUID()
    val item = Item(itemId,
        "Toy",
        "Very rare")
    auction.item = itemId
    val buyerId = "buyerId"
    val bid1 = auction.createBid(
        BidCreateDto(BigDecimal(125),
            LocalDateTime.now(),
            buyerId),
        bidFactory,
        bidRepository
    )
    auctionRepository.save(auction)
    return auction
}

fun setBidInfo(buyerId: String): BidCreateDto {
    return BidCreateDto(BigDecimal(100.00), LocalDateTime.now(), buyerId)
}
