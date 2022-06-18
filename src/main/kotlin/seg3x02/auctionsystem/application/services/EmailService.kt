package seg3x02.auctionsystem.application.services

interface EmailService {
    fun sendSellerCompletedAuctionPaymentProcessedFine(email: String)
    fun sendWinnerCompletedAuctionEmailWithWinningBid(email: String)
    fun sendBidUnsuccessfulEmail(email: String)
    fun sendSellerCompletedAuctionEmailNoWinningBid(email: String)
    fun sendSellerCompletedAuctionPaymentProcessFail(email: String)
    fun sendAccountDeactivationEmail(email: String)
}
