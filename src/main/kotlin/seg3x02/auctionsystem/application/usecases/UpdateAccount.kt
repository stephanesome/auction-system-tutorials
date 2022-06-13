package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.application.dtos.queries.AccountCreateDto

interface UpdateAccount {
    fun updateAccount(accountId: String, accountInfo: AccountCreateDto): Boolean
}
