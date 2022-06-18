package seg3x02.auctionsystem.domain.auction.entities

import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class Auction(
    val id: UUID,
    private val startTime: LocalDateTime,
    val duration: Duration,
    private val startPrice: BigDecimal,
    private val minIncrement: BigDecimal,
    val seller: String,
    val category: AuctionCategory,
    var isclosed: Boolean
) {
    lateinit var item: UUID
    lateinit var fee: BigDecimal
    val bids: MutableList<UUID> = ArrayList()

    fun createBid(bidInfo: BidCreateDto, bidFactory: BidFactory, bidRepository: BidRepository): UUID? {
        return if (startTime.isBefore(LocalDateTime.now()) &&
            (bidInfo.amount >= minimumBidAmount(bidRepository))) {
            newBid(bidInfo, bidFactory, bidRepository)
        } else {
            null
        }
    }

    private fun minimumBidAmount(bidRepository: BidRepository): BigDecimal {
        if (bids.isNotEmpty()) {
            val lastBidId = bids.last()
            val lastBid = bidRepository.find(lastBidId)
            if (lastBid != null) return lastBid.amount.plus(this.minIncrement)
        }
        return this.startPrice
    }

    private fun newBid(bidInfo: BidCreateDto, bidFactory: BidFactory, bidRepository: BidRepository): UUID {
        val newBid = bidFactory.createBid(bidInfo)
        bids.add(newBid.id)
        bidRepository.save(newBid)
        return newBid.id
    }

    fun close(): UUID? {
        isclosed = true
        return bids.lastOrNull()
    }

    fun getBidder(bidId: UUID, bidRepository: BidRepository): String? {
        return bidRepository.find(bidId)?.buyer
    }

    fun closeTime(): LocalDateTime {
        return startTime.plus(duration)
    }
}

