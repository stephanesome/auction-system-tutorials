package seg3x02.auctionsystem.contracts.steps

import io.cucumber.java8.En
import io.cucumber.java8.PendingException
import org.assertj.core.api.Assertions
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.application.usecases.PlaceBid
import seg3x02.auctionsystem.application.usecases.implementation.PlaceBidImpl
import seg3x02.auctionsystem.contracts.testStubs.factories.AuctionFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.BidFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.CreditCardFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.ItemFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.AccountRepositoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.AuctionRepositoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.BidRepositoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.CreditCardRepositoryStub
import seg3x02.auctionsystem.contracts.testStubs.services.AuctionFeeCalculatorStub
import seg3x02.auctionsystem.contracts.testStubs.services.CreditServiceStub
import seg3x02.auctionsystem.contracts.testStubs.services.EventEmitterStub
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.Bid
import seg3x02.auctionsystem.domain.auction.facade.implementation.AuctionFacadeImpl
import seg3x02.auctionsystem.domain.item.facade.implementation.ItemFacadeImpl
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.facade.implementation.UserFacadeImpl
import java.util.*

class PlaceBidStepDefs: En {
    private var accountRepository = AccountRepositoryStub()
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

    private var buyer: UserAccount? = null
    private var auction: Auction? = null
    private var bidInfo: BidCreateDto? = null
    private var newBidId: UUID? = null
    private var newBid: Bid? = null
    init {
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
        When("the application command placeBid is executed") {
            val userFacade = UserFacadeImpl(accountRepository,
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
    }
}
