package id.co.integrapratama.bnipayment.feature_sale

import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds

sealed class SaleUiEvent {
    data class SetContactless(val isContactless: Boolean) : SaleUiEvent()
    data class OnAmountChange(val amount: String) : SaleUiEvent()
    data class OnTipChange(val tip: String) : SaleUiEvent()
    data object OnConfirmCard : SaleUiEvent()

    data class MappingPinpad(
        val containerInfo: CustomPinpadUiBounds,
        val pinpadMap: List<CustomPinpadUiBounds>,
    ) : SaleUiEvent()

    data class MappingOfflinePinpad(
        val containerInfo: CustomPinpadUiBounds,
        val pinpadMap: List<CustomPinpadUiBounds>,
    ) : SaleUiEvent()
}