package seg3x02.auctionsystem.contracts.testStubs.services

import seg3x02.auctionsystem.application.services.EmailService

class EmailServiceStub : EmailService {
    override fun sendSellerCompletedAuctionPaymentProcessedFine(email: String) {

    }

    override fun sendWinnerCompletedAuctionEmailWithWinningBid(email: String) {

    }

    override fun sendBidUnsuccessfulEmail(email: String) {

    }

    override fun sendSellerCompletedAuctionEmailNoWinningBid(email: String) {

    }

    override fun sendSellerCompletedAuctionPaymentProcessFail(email: String) {

    }

    override fun sendAccountDeactivationEmail(email: String) {

    }

}
