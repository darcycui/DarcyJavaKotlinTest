package org.example.copy

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
    println("state=${System.identityHashCode(state)}")
    println("state.accountsState=${System.identityHashCode(state.accountsState)}")
    println("accountsState.accountsShown=${System.identityHashCode(state.accountsState.accountsShown)}")
    println("list-->${state.accountsState.accountsShown}")

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
    println("state=${System.identityHashCode(stateNew)}")
    println("state.accountsState=${System.identityHashCode(stateNew.accountsState)}")
    println("accountsState.accountsShown=${System.identityHashCode(stateNew.accountsState.accountsShown)}")
    println("list-->${stateNew.accountsState.accountsShown}")
    println("stateNew==state ${stateNew == state}")
    println("stateNew.accountsState==state.accountsState ${stateNew.accountsState == state.accountsState}")
    println("stateNew.accountsState.accountsShown==state.accountsState.accountsShown ${stateNew.accountsState.accountsShown == state.accountsState.accountsShown}")


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