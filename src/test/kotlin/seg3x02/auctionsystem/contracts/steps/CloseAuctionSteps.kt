package seg3x02.auctionsystem.contracts.steps

import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import org.assertj.core.api.Assertions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.usecases.CloseAuction
import seg3x02.auctionsystem.application.usecases.implementation.CloseAuctionImpl
import seg3x02.auctionsystem.contracts.testStubs.factories.AuctionFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.BidFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.CreditCardFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.factories.ItemFactoryStub
import seg3x02.auctionsystem.contracts.testStubs.repositories.*
import seg3x02.auctionsystem.contracts.testStubs.services.EmailServiceStub
import seg3x02.auctionsystem.contracts.testStubs.services.EventEmitterStub
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.facade.implementation.AuctionFacadeImpl
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.facade.implementation.UserFacadeImpl
import java.time.LocalDateTime

class CloseAuctionSteps: En {
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
    private var emailService = EmailServiceStub()
    @Mock
    private lateinit var creditService: CreditService

    private var seller: UserAccount? = null
    private var auction: Auction? = null
    init {
        Before { _: Scenario ->
            MockitoAnnotations.openMocks(this)
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
        When("the application command closeAuction is invoked") {
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
            val uc: CloseAuction = CloseAuctionImpl(
                userFacade, auctionFacade, creditService, emailService
            )
            auction?.let { uc.closeAuction(it.id) }
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
    }

}
