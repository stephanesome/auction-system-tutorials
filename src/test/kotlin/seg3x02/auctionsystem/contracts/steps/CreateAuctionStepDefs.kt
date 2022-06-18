package seg3x02.auctionsystem.contracts.steps

import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import org.assertj.core.api.Assertions
import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.application.usecases.CreateAuction
import seg3x02.auctionsystem.application.usecases.implementation.CreateAuctionImpl
import seg3x02.auctionsystem.contracts.testStubs.factories.AuctionFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.BidFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.CreditCardFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.ItemFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.*
import seg3x02.auctionsystem.contracts.testStubs.services.AuctionFeeCalculatorStub
import seg3x02.auctionsystem.contracts.testStubs.services.CreditServiceStub
import seg3x02.auctionsystem.contracts.testStubs.services.EventEmitterStub
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.facade.implementation.AuctionFacadeImpl
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.facade.implementation.ItemFacadeImpl
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.facade.implementation.UserFacadeImpl
import java.math.BigDecimal
import java.util.*

class CreateAuctionStepDefs: En {
    private var accountRepository = AccountRepositoryStub()
    private var itemRepository = ItemRepositoryStub()
    private var auctionRepository = AuctionRepositoryStub()
    private var creditCardRepository = CreditCardRepositoryStub()
    private var bidRepository = BidRepositoryStub()
    private var auctionFactory = AuctionFactoryStub()
    private var creditCardFactory = CreditCardFactoryStub()
    private var itemFactory = ItemFactoryStub()
    private var bidFactory = BidFactoryStub()
    private var eventEmitter = EventEmitterStub()
    private var creditService = CreditServiceStub()
    private var auctionFeeCalculator = AuctionFeeCalculatorStub()

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

    init {
        Given("the seller is signed in") {
            seller = createAccount(accountRepository)
            Assertions.assertThat(seller).isNotNull
        }
        Given("the seller has no pending payment") {
            Assertions.assertThat(seller?.pendingPayment).isNull()
        }
        Given("the seller has a credit card") {
            seller?.let { addCreditCardToAccount(it) }
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
        When("the application command addAuction is invoked") {
            val userFacade = UserFacadeImpl(accountRepository,
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
        }
    }
}
