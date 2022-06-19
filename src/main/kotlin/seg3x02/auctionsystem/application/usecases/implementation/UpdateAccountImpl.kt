package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.application.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.application.usecases.UpdateAccount
import seg3x02.auctionsystem.domain.user.facade.UserFacade

class UpdateAccountImpl(
    private var userFacade: UserFacade): UpdateAccount {

    override fun updateAccount(accountId: String, accountInfo: AccountCreateDto): Boolean {
        return userFacade.updateAccount(accountId, accountInfo)
    }
}
