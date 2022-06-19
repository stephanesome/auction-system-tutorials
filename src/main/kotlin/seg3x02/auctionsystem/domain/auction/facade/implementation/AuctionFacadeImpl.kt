package seg3x02.auctionsystem.domain.auction.facade.implementation

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.auction.events.AuctionClosed
import seg3x02.auctionsystem.domain.auction.events.NewAuctionAdded
import seg3x02.auctionsystem.domain.auction.events.NewBidCreated
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.auction.factories.AuctionFactory
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import java.math.BigDecimal
import java.util.*

class AuctionFacadeImpl(
    private var auctionFactory: AuctionFactory,
    private var auctionRepository: AuctionRepository,
    private var bidFactory: BidFactory,
    private var bidRepository: BidRepository,
    private var eventEmitter: DomainEventEmitter): AuctionFacade {

    override fun addAuction(auctionInfo: AuctionCreateDto, aucItemId: UUID): UUID {
        val auction = auctionFactory.createAuction(auctionInfo, aucItemId)
        auctionRepository.save(auction)
        eventEmitter.emit(NewAuctionAdded(UUID.randomUUID(), Date(), auction.id))
        return auction.id
    }

    override fun closeAuction(auctionId: UUID): String? {
        val auc = auctionRepository.find(auctionId)
        if (auc != null) {
            val winnerBidId = auc.close()
            auctionRepository.save(auc)
            eventEmitter.emit(
                AuctionClosed(UUID.randomUUID(),
                    Date(),
                    auc.id,
                    winnerBidId)
            )
            if (winnerBidId != null) {
                return auc.getBidder(winnerBidId, bidRepository)
            }
        }
        return null
    }

    override fun getAuctionSeller(auctionId: UUID): String? {
        return auctionRepository.find(auctionId)?.seller
    }

    override fun setAuctionFee(auctionId: UUID, fee: BigDecimal) {
        val auction = auctionRepository.find(auctionId)
        if (auction != null) {
            auction.fee = fee
        }
    }

    override fun getAuctionFee(auctionId: UUID): BigDecimal? {
        val auction = auctionRepository.find(auctionId)
        return auction?.fee
    }

    override fun placeBid(auctionId: UUID, bidInfo: BidCreateDto): UUID? {
        val auction = auctionRepository.find(auctionId)
        val bidId = auction?.createBid(bidInfo, bidFactory, bidRepository)
        return if (bidId != null) {
            auctionRepository.save(auction)
            val newBidEvent = NewBidCreated(UUID.randomUUID(),
                Date(),
                bidId)
            eventEmitter.emit(newBidEvent)
            bidId
        } else null
    }

    override fun includesAuctionsInProgress(auctionIds: List<UUID>): Boolean {
        for (auc in auctionIds) {
            val auction = auctionRepository.find(auc)
            if (auction != null && ! auction.isclosed) return true
        }
        return false
    }
}
