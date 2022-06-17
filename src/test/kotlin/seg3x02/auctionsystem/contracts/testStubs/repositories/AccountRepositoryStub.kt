package seg3x02.auctionsystem.contracts.testStubs.repositories

import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import java.util.*

class AccountRepositoryStub : AccountRepository {
    private val users: MutableMap<String, UserAccount> = HashMap()

    override fun save(user: UserAccount): UserAccount {
        users[user.id] = user
        return user
    }

    override fun find(userId: String): UserAccount? = users[userId]
}
