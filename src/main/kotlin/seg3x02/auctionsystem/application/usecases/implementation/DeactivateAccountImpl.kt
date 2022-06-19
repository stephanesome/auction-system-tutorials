package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.application.services.EmailService
import seg3x02.auctionsystem.application.usecases.DeactivateAccount
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import java.util.*

class DeactivateAccountImpl(private val userFacade: UserFacade,
                            private val auctionFacade: AuctionFacade,
                            private val emailService: EmailService): DeactivateAccount {
    override fun deactivateAccount(accountId: String): Boolean {
        val userAuctions = userFacade.getUserAuctions(accountId)
        return if (auctionFacade.includesAuctionsInProgress(userAuctions) ||
            userFacade.hasPendingPayment(accountId)) {
            false
        } else {
            userFacade.deactivateAccount(accountId)
            // send email that account has been deactivated
            val email = userFacade.getUserEmailAddress(accountId)
            if (email != null) {
                emailService.sendAccountDeactivationEmail(email)
            }
            true
        }
    }
}
