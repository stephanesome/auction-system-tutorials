package seg3x02.auctionsystem.contracts.steps

import io.cucumber.java8.En
import io.cucumber.java8.PendingException
import io.cucumber.java8.Scenario
import org.assertj.core.api.Assertions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import seg3x02.auctionsystem.application.dtos.queries.*
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.usecases.CloseAuction
import seg3x02.auctionsystem.application.usecases.CreateAccount
import seg3x02.auctionsystem.application.usecases.CreateAuction
import seg3x02.auctionsystem.application.usecases.PlaceBid
import seg3x02.auctionsystem.application.usecases.implementation.CloseAuctionImpl
import seg3x02.auctionsystem.application.usecases.implementation.CreateAccountImpl
import seg3x02.auctionsystem.application.usecases.implementation.CreateAuctionImpl
import seg3x02.auctionsystem.application.usecases.implementation.PlaceBidImpl
import seg3x02.auctionsystem.contracts.testStubs.factories.*
import seg3x02.auctionsystem.contracts.testStubs.repositories.*
import seg3x02.auctionsystem.contracts.testStubs.services.AuctionFeeCalculatorStub
import seg3x02.auctionsystem.contracts.testStubs.services.EmailServiceStub
import seg3x02.auctionsystem.contracts.testStubs.services.EventEmitterStub
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.Bid
import seg3x02.auctionsystem.domain.auction.facade.implementation.AuctionFacadeImpl
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.facade.implementation.ItemFacadeImpl
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.facade.implementation.UserFacadeImpl
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class StepsDefinition: En {
    private var accountRepository = AccountRepositoryStub()
    private var accountFactory = AccountFactoryStub()
    private var itemRepository = ItemRepositoryStub()
    private var auctionRepository = AuctionRepositoryStub()
    private var creditCardRepository = CreditCardRepositoryStub()
    private var bidRepository = BidRepositoryStub()
    private var auctionFactory = AuctionFactoryStub()
    private var creditCardFactory = CreditCardFactoryStub()
    private var itemFactory = ItemFactoryStub()
    private var bidFactory = BidFactoryStub()
    private var eventEmitter = EventEmitterStub()
    private var emailService = EmailServiceStub()
    private var auctionFeeCalculator = AuctionFeeCalculatorStub()
    @Mock
    private lateinit var creditService: CreditService

    private var seller: UserAccount? = null
    private var itemInfo: ItemCreateDto? = null
    private var auctionInfo: AuctionCreateDto? = null
    private var creditCardInfo: CreditCardCreateDto? = null
    private var newAucId: UUID? = null
    private var newauction: Auction? = null
    private var newItemId: UUID? = null
    private var newItem: Item? = null
    private var newCCnum: String? = null
    private var newCC: CreditCard? = null
    private var buyer: UserAccount? = null
    private var auction: Auction? = null
    private var bidInfo: BidCreateDto? = null
    private var newBidId: UUID? = null
    private var newBid: Bid? = null
    private var accountInfo: AccountCreateDto? = null
    private var newAccount: UserAccount? = null

    init {
        Before { _: Scenario ->
            MockitoAnnotations.openMocks(this)
        }
        Given("the seller is signed in") {
            seller = createAccount(accountRepository)
            Assertions.assertThat(seller).isNotNull
        }
        Given("the seller has no pending payment") {
            Assertions.assertThat(seller?.pendingPayment).isNull()
        }
        Given("the seller has a credit card") {
            seller?.let { addCreditCardToAccount(it, creditCardRepository) }
            Assertions.assertThat(seller?.creditCardNumber).isNotNull
        }
        Given("auction information is provided") {
            itemInfo = setItemInfo()
            auctionInfo = seller?.let { setAuctionInfo(it.id, itemInfo!!) }
            Assertions.assertThat(auctionInfo).isNotNull
        }
        Given(
            "the auction information does not include credit card information"
        ) {
            Assertions.assertThat(auctionInfo?.creditCardInfo).isNull()
        }
        Given("the auction information includes credit card information") {
            creditCardInfo = setCreditCardInfo()
            auctionInfo?.creditCardInfo = creditCardInfo
            Assertions.assertThat(auctionInfo?.creditCardInfo).isNotNull
        }
        Given("the buyer is signed in") {
            buyer =  createAccount(accountRepository)
        }
        Given("the buyer has no pending payment") {
            Assertions.assertThat(buyer?.pendingPayment).isNull()
        }
        Given("the auction is open") {
            auction = createAuction(auctionRepository)
            Assertions.assertThat(auction).isNotNull
            Assertions.assertThat(auction?.isclosed).isFalse()
        }
        Given("bid information is provided") {
            bidInfo = buyer?.let { setBidInfo(it.id) }
            Assertions.assertThat(bidInfo).isNotNull
        }
        Given("the auction deadline has passed") {
            seller = createAccount(accountRepository)
            auction = createAuctionForSeller(seller!!.id, auctionRepository, bidFactory, bidRepository)
            Assertions.assertThat(auction).isNotNull
            Assertions.assertThat(auction!!.isclosed).isFalse
            Assertions.assertThat(auction!!.closeTime().isBefore(LocalDateTime.now())).isTrue()
        }
        Given(
            "the auction seller credit card is able to settle the auction fee"
        ) {
            seller?.let { addCreditCardToAccount(it, creditCardRepository) }
            Assertions.assertThat(seller?.creditCardNumber).isNotNull
            val cc = seller?.creditCardNumber?.let { creditCardRepository.find(it) }
            Mockito.`when`(creditService.processPayment(
                cc!!.number,
                cc.expirationMonth,
                cc.expirationYear,
                auction!!.fee
            )).thenReturn(true)
        }
        Given(
            "the auction seller credit card is unable to settle the auction fee"
        ) {
            seller?.let { addCreditCardToAccount(it, creditCardRepository) }
            Assertions.assertThat(seller?.creditCardNumber).isNotNull
            val cc = seller?.creditCardNumber?.let { creditCardRepository.find(it) }
            Mockito.`when`(creditService.processPayment(
                cc!!.number,
                cc.expirationMonth,
                cc.expirationYear,
                auction!!.fee
            )).thenReturn(false)
        }
        Given("provided account information doesn't match an existing user account") {
            accountInfo = setAccountInfo()
            val user = accountRepository.find(accountInfo!!.userName)
            Assertions.assertThat(user).isNull()
        }
        Given("provided account information includes credit card information") {
            creditCardInfo = setCreditCardInfo()
            accountInfo?.creditCardInfo = creditCardInfo
            Assertions.assertThat(accountInfo?.creditCardInfo).isNotNull
        }
        Given(
            "provided account information does not include credit card information") {
            Assertions.assertThat(accountInfo?.creditCardInfo).isNull()
        }
        When("the application command addAuction is invoked") {
            val userFacade = UserFacadeImpl(accountRepository,
                accountFactory,
                creditCardRepository,
                creditCardFactory,
                eventEmitter,
                creditService)
            val itemFacade = ItemFacadeImpl(itemFactory,
                itemRepository,
                eventEmitter)
            val auctionFacade = AuctionFacadeImpl(
                auctionFactory,
                auctionRepository,
                bidFactory,
                bidRepository,
                eventEmitter)
            val uc: CreateAuction = CreateAuctionImpl(
                userFacade, itemFacade, auctionFacade, auctionFeeCalculator
            )
            newAucId = auctionInfo?.let { uc.addAuction(it) }
        }
        When("the application command placeBid is executed") {
            val userFacade = UserFacadeImpl(accountRepository,
                accountFactory,
                creditCardRepository,
                creditCardFactory,
                eventEmitter,
                creditService)
            val auctionFacade = AuctionFacadeImpl(
                auctionFactory,
                auctionRepository,
                bidFactory,
                bidRepository,
                eventEmitter)
            val uc: PlaceBid = PlaceBidImpl(userFacade, auctionFacade)
            newBidId = bidInfo?.let {
                buyer?.let { it1 -> auction?.let { it2 -> uc.placeBid(it1.id, it2.id, it) } } }
        }
        When("the application command closeAuction is invoked") {
            val userFacade = UserFacadeImpl(accountRepository,
                accountFactory,
                creditCardRepository,
                creditCardFactory,
                eventEmitter,
                creditService)
            val auctionFacade = AuctionFacadeImpl(
                auctionFactory,
                auctionRepository,
                bidFactory,
                bidRepository,
                eventEmitter)
            val uc: CloseAuction = CloseAuctionImpl(
                userFacade, auctionFacade, creditService, emailService
            )
            auction?.let { uc.closeAuction(it.id) }
        }
        When("the application command createAccount is invoked") {
            val userFacade = UserFacadeImpl(accountRepository,
                accountFactory,
                creditCardRepository,
                creditCardFactory,
                eventEmitter,
                creditService)
            val uc: CreateAccount = CreateAccountImpl(userFacade)
            accountInfo?.let { uc.createAccount(it) }
        }
        Then("a new auction is created") {
            Assertions.assertThat(newAucId).isNotNull
        }
        Then("the new auction is initialized from the auction information") {
            newauction = newAucId?.let { auctionRepository.find(it) }
            Assertions.assertThat(newauction).isNotNull
            Assertions.assertThat(newauction?.seller).isEqualTo(auctionInfo?.seller)
            Assertions.assertThat(newauction?.category?.name).isEqualTo(auctionInfo?.category)
        }
        Then("the new auction processing fee has been set") {
            Assertions.assertThat(newauction?.fee).isEqualTo(BigDecimal(10))
        }
        Then("the new auction is added to the seller's auctions") {
            Assertions.assertThat(seller?.auctions?.contains(newAucId)).isTrue()
        }
        Then("a new item is created") {
            newItemId = eventEmitter.retrieveNewItemAddedEvent().itemId
            Assertions.assertThat(newItemId).isNotNull
        }
        Then("the new item is initialized from the auction information") {
            newItem = newItemId?.let { itemRepository.find(it) }
            Assertions.assertThat(newItem).isNotNull
            Assertions.assertThat(newItem?.title).isEqualTo(itemInfo?.title)
            Assertions.assertThat(newItem?.description).isEqualTo(itemInfo?.description)
        }
        Then("the new auction is linked to the new item") {
            Assertions.assertThat(newauction?.item).isEqualTo(newItemId)
        }
        Then("a new credit card is created") {
            newCCnum = eventEmitter.retrieveCreditCardCreatedEvent().creditCardNumber
            Assertions.assertThat(newCCnum).isNotNull
        }
        Then(
            "the new credit card is initialized from the credit card information"
        ) {
            newCC = newCCnum?.let { creditCardRepository.find(it) }
            Assertions.assertThat(newCC).isNotNull
            Assertions.assertThat(newCC?.number).isEqualTo(creditCardInfo?.number)
            Assertions.assertThat(newCC?.accountFirstname).isEqualTo(creditCardInfo?.accountFirstname)
            Assertions.assertThat(newCC?.accountAddress?.city).isEqualTo(creditCardInfo?.accountAddress?.city)
        }
        Then("the new credit card is set as the seller credit card") {
            Assertions.assertThat(seller?.creditCardNumber).isEqualTo(newCC?.number)
        }
        Then("a new bid is created") {
            Assertions.assertThat(newBidId).isNotNull
        }
        Then("the new is initialized from the bid information") {
            newBid = newBidId?.let { bidRepository.find(it) }
            Assertions.assertThat(newBid).isNotNull
            Assertions.assertThat(newBid?.buyer).isEqualTo(bidInfo?.buyer)
            Assertions.assertThat(newBid?.amount).isEqualTo(bidInfo?.amount)
        }
        Then("the new bid is added to the auction bids") {
            auction?.bids?.let { Assertions.assertThat(it.contains(newBidId)).isTrue() }
        }
        Then("the new bid is added to the buyer bids") {
            Assertions.assertThat(newBidId?.let { buyer?.bids?.contains(it)}).isTrue()
        }
        Then("the auction is marked as closed") {
            Assertions.assertThat(auction?.isclosed).isTrue()
        }
        Then(
            "the auction fee is set as pending payment to the auction seller account"
        ) {
            Assertions.assertThat(seller?.pendingPayment).isNotNull
            Assertions.assertThat(seller?.pendingPayment?.amount).isEqualTo(auction?.fee)
        }
        Then("a new user account is created") {
            newAccount = accountInfo?.let { accountRepository.find(it.userName) }
            Assertions.assertThat(newAccount).isNotNull
        }
        Then("the new user account is initialized from the account information") {
            Assertions.assertThat(newAccount).isNotNull
        }
        Then("the new user account is set as active") {
            newAccount?.let { Assertions.assertThat(it.active).isTrue() }
        }
        Then("the new credit card is set as the user account credit card") {
            Assertions.assertThat(newAccount?.creditCardNumber).isEqualTo(newCCnum)
        }
        After { _: Scenario ->
            seller = null
            itemInfo = null
            auctionInfo = null
            creditCardInfo = null
            newAucId = null
            newauction = null
            newItemId = null
            newItem = null
            newCCnum = null
            newCC = null

            accountRepository = AccountRepositoryStub()
            itemRepository = ItemRepositoryStub()
            creditCardRepository = CreditCardRepositoryStub()
            auctionRepository = AuctionRepositoryStub()
            eventEmitter = EventEmitterStub()
        }
    }
}
