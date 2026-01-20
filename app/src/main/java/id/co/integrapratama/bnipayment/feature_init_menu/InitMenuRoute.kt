package id.co.integrapratama.bnipayment.feature_init_menu

import kotlinx.serialization.Serializable

sealed interface InitMenuRoute {
    @Serializable
    object Menu : InitMenuRoute
}