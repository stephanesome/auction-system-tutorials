package seg3x02.auctionsystem.contracts.steps

import io.cucumber.java8.En
import io.cucumber.java8.PendingException
import org.assertj.core.api.Assertions
import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.application.usecases.CreateAuction
import seg3x02.auctionsystem.application.usecases.implementation.CreateAuctionImpl
import seg3x02.auctionsystem.contracts.testStubs.factories.AuctionFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.CreditCardFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.ItemFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.AccountRepositoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.AuctionRepositoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.CreditCardRepositoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.ItemRepositoryStub
import seg3x02.auctionsystem.contracts.testStubs.services.AuctionFeeCalculatorStub
import seg3x02.auctionsystem.contracts.testStubs.services.EventEmitterStub
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.facade.implementation.AuctionFacadeImpl
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.facade.implementation.ItemFacadeImpl
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.facade.implementation.UserFacadeImpl
import java.math.BigDecimal
import java.util.*

class CreateAuctionStepDefs: En {
    private var accountRepository = AccountRepositoryStub()
    private var itemRepository = ItemRepositoryStub()
    private var auctionRepository = AuctionRepositoryStub()
    private var creditCardRepository = CreditCardRepositoryStub()
    private var auctionFactory = AuctionFactoryStub()
    private var creditCardFactory = CreditCardFactoryStub()
    private var itemFactory = ItemFactoryStub()
    private var eventEmitter = EventEmitterStub()
    private var auctionFeeCalculator = AuctionFeeCalculatorStub()

    lateinit var seller: UserAccount
    lateinit var itemInfo: ItemCreateDto
    lateinit var auctionInfo: AuctionCreateDto
    var newAucId: UUID? = null
    var newauction: Auction? = null
    var newItemId: UUID? = null
    var newItem: Item? = null

    init {
        Given("the seller is signed in") {
            seller = createAccount(accountRepository)
            Assertions.assertThat(seller).isNotNull
        }
        Given("the seller has no pending payment") {
            Assertions.assertThat(seller.pendingPayment).isNull()
        }
        Given("the seller has a credit card") {
            addCreditCardToAccount(seller)
            Assertions.assertThat(seller.creditCardNumber).isNotNull()
        }
        Given("auction information is provided") {
            itemInfo = setItemInfo()
            auctionInfo = setAuctionInfo(seller.id, itemInfo)
            Assertions.assertThat(auctionInfo).isNotNull
        }
        Given(
            "the auction information does not include credit card information"
        ) {
            Assertions.assertThat(auctionInfo.creditCardInfo).isNull()
        }
        Given("the auction information includes credit card information") { throw PendingException() }
        When("the application command addAuction is invoked") {
            val userFacade = UserFacadeImpl(accountRepository,
                                            creditCardRepository,
                                            creditCardFactory,
                                            eventEmitter)
            val itemFacade = ItemFacadeImpl(itemFactory,
                                            itemRepository,
                                            eventEmitter)
            val auctionFacade = AuctionFacadeImpl(
                                    auctionFactory,
                                    auctionRepository,
                                    eventEmitter)
            val uc: CreateAuction = CreateAuctionImpl(
                userFacade, itemFacade, auctionFacade, auctionFeeCalculator
            )
            newAucId = uc.addAuction(auctionInfo)
        }
        Then("a new auction is created") {
            Assertions.assertThat(newAucId).isNotNull
        }
        Then("the new auction is initialized from the auction information") {
            newauction = newAucId?.let { auctionRepository.find(it) }
            Assertions.assertThat(newauction).isNotNull
            Assertions.assertThat(newauction?.seller).isEqualTo(auctionInfo.seller)
            Assertions.assertThat(newauction?.category?.name).isEqualTo(auctionInfo.category)
        }
        Then("the new auction processing fee has been set") {
            Assertions.assertThat(newauction?.fee).isEqualTo(BigDecimal(10))
        }
        Then("the new auction is added to the seller's auctions") {
            Assertions.assertThat(seller.auctions.contains(newAucId)).isTrue()
        }
        Then("a new item is created") {
            newItemId = eventEmitter.retrieveNewItemAddedEvent()?.itemId
            Assertions.assertThat(newItemId).isNotNull
        }
        Then("the new item is initialized from the auction information") {
            newItem = newItemId?.let { itemRepository.find(it) }
            Assertions.assertThat(newItem).isNotNull
            Assertions.assertThat(newItem?.title).isEqualTo(itemInfo.title)
            Assertions.assertThat(newItem?.description).isEqualTo(itemInfo.description)
        }
        Then("the new auction is linked to the new item") {
            Assertions.assertThat(newauction?.item).isEqualTo(newItemId)
        }
        Then("a new credit card is created") { throw PendingException() }
        Then(
            "the new credit card is initialized from the credit card information"
        ) { throw PendingException() }
        Then("the new credit card is set as the seller credit card") { throw PendingException() }

    }
}
