package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.application.dtos.queries.AccountCreateDto

interface CreateAccount {
    fun createAccount(accountInfo: AccountCreateDto): Boolean
}
