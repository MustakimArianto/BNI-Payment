package id.co.integrapratama.bnipayment.feature_tap_cash

import kotlinx.serialization.Serializable

sealed interface TapCashRoute {
    @Serializable
    data object Menu : TapCashRoute
}