package id.co.integrapratama.bnipayment.feature_menu

import kotlinx.serialization.Serializable

sealed class MenuEvent {
    @Serializable
    data object DisableMenuChecking : MenuEvent()
}