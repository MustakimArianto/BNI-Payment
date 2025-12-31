package id.co.integrapratama.bnipayment.feature_mini_atm

import kotlinx.serialization.Serializable

sealed interface MiniATMRoute {
    @Serializable
    data object Menu : MiniATMRoute
    
    @Serializable
    data object BalanceInfo : MiniATMRoute
    
    @Serializable
    data object Transfer : MiniATMRoute
    
    @Serializable
    data object Purchase : MiniATMRoute
}