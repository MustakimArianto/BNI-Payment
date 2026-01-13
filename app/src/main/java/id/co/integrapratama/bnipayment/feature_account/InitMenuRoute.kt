package id.co.integrapratama.bnipayment.feature_account

import kotlinx.serialization.Serializable

sealed interface InitMenuRoute {
    @Serializable
    object Menu : InitMenuRoute
}