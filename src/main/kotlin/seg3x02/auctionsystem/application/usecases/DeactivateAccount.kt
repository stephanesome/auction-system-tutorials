package seg3x02.auctionsystem.application.usecases

import java.util.*

interface DeactivateAccount {
    fun deactivateAccount(accountId: String): Boolean
}
