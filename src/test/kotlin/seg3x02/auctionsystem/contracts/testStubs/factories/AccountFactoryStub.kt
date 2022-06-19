package seg3x02.auctionsystem.contracts.testStubs.factories

import seg3x02.auctionsystem.application.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.factories.AccountFactory

class AccountFactoryStub: AccountFactory {
    override fun createAccount(accountInfo: AccountCreateDto): UserAccount {
        return UserAccount(accountInfo.userName,
                        accountInfo.firstname,
                        accountInfo.lastname,
                        accountInfo.password,
                        accountInfo.email)
    }
}
