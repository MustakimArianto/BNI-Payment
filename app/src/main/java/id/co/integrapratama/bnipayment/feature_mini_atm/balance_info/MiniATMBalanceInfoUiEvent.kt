package id.co.integrapratama.bnipayment.feature_mini_atm.balance_info

import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds

sealed class MiniATMBalanceInfoUiEvent {
    data class SetContactless(val isContactless: Boolean) : MiniATMBalanceInfoUiEvent()
    data object OnConfirmCard : MiniATMBalanceInfoUiEvent()
    data class MappingPinpad(
        val containerInfo: CustomPinpadUiBounds,
        val pinpadMap: List<CustomPinpadUiBounds>,
    ) : MiniATMBalanceInfoUiEvent()

    data class MappingOfflinePinpad(
        val containerInfo: CustomPinpadUiBounds,
        val pinpadMap: List<CustomPinpadUiBounds>,
    ) : MiniATMBalanceInfoUiEvent()
}