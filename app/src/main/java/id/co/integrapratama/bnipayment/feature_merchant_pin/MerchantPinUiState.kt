package id.co.integrapratama.bnipayment.feature_merchant_pin

data class MerchantPinUiState(
    val merchantPin: String = "",
    val checkingMerchantPinStatus: Int = -1,
    val loadingMessage: String = ""
)