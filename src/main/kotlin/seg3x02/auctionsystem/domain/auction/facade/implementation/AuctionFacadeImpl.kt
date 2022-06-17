package seg3x02.auctionsystem.domain.auction.facade.implementation

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.auction.events.NewAuctionAdded
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.auction.factories.AuctionFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import java.math.BigDecimal
import java.util.*

class AuctionFacadeImpl(
    private var auctionFactory: AuctionFactory,
    private var auctionRepository: AuctionRepository,
    private var eventEmitter: DomainEventEmitter): AuctionFacade {

    override fun addAuction(auctionInfo: AuctionCreateDto, aucItemId: UUID): UUID {
        val auction = auctionFactory.createAuction(auctionInfo, aucItemId)
        auctionRepository.save(auction)
        eventEmitter.emit(NewAuctionAdded(UUID.randomUUID(), Date(), auction.id))
        return auction.id
    }

    override fun setAuctionFee(auctionId: UUID, fee: BigDecimal) {
        val auction = auctionRepository.find(auctionId)
        if (auction != null) {
            auction.fee = fee
        }
    }
}
