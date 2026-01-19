package id.co.integrapratama.bnipayment.feature_merchant_pin

sealed class MerchantPinUiEvent {
    data class ChangeMerchantPin(val pin: String) : MerchantPinUiEvent()
    data object SubmitMerchantPin : MerchantPinUiEvent()
}