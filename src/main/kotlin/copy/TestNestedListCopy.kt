package copy
import exts.logD

fun main() {
    val accountsState = AccountsState(
        accountsShown = listOf(
            AccountInfo("100", false),
            AccountInfo("200", false)
        )
    )
    val state = AccountManageState(
        accountsState = accountsState
    )
    logD(message = "state=${System.identityHashCode(state)}")
    logD(message = "state.accountsState=${System.identityHashCode(state.accountsState)}")
    logD(message = "accountsState.accountsShown=${System.identityHashCode(state.accountsState.accountsShown)}")
    logD(message = "list-->${state.accountsState.accountsShown}")

//    val stateNew = state.copy(
//        accountsState = state.accountsState.copy(
//            accountsShown = state.accountsState.accountsShown.
//                map { accountInfo ->
//                    accountInfo.copy(isSelected = true)
//                }
//        )
//    )
    val accountsShownNew = state.accountsState.accountsShown.map { accountInfo ->
        accountInfo.copy(isSelected = true)
    }
    val stateNew = state.copy(
        accountsState = state.accountsState.copy(
            accountsShown = accountsShownNew
        )
    )
    logD(message = "state=${System.identityHashCode(stateNew)}")
    logD(message = "state.accountsState=${System.identityHashCode(stateNew.accountsState)}")
    logD(message = "accountsState.accountsShown=${System.identityHashCode(stateNew.accountsState.accountsShown)}")
    logD(message = "list-->${stateNew.accountsState.accountsShown}")
    logD(message = "stateNew==state ${stateNew == state}")
    logD(message = "stateNew.accountsState==state.accountsState ${stateNew.accountsState == state.accountsState}")
    logD(message = "stateNew.accountsState.accountsShown==state.accountsState.accountsShown ${stateNew.accountsState.accountsShown == state.accountsState.accountsShown}")


}

data class AccountManageState(
    val accountsState: AccountsState = AccountsState()
)

data class AccountsState(
    // darcyRefactor: 使用不可变列表List 替代可变列表MutableList，并在每次变更时产生新实例
    val accountsShown: List<AccountInfo> = emptyList()
)

data class AccountInfo(
    val accountId: String = "",
    val isSelected: Boolean = false
)