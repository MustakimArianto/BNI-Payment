package id.co.integrapratama.bnipayment.feature_mini_atm.balance_info

import kotlinx.serialization.Serializable

interface MiniATMBalanceInfoRoute {
    @Serializable
    data object Graph : MiniATMBalanceInfoRoute

    @Serializable
    data object InsertCard : MiniATMBalanceInfoRoute

    @Serializable
    data object ConfirmCard : MiniATMBalanceInfoRoute

    @Serializable
    data object TransactionStatus : MiniATMBalanceInfoRoute
}