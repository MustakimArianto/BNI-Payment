package id.co.integrapratama.bnipayment.feature_sale

sealed class SaleUiEvent {
    data class OnAmountChange(val amount: String) : SaleUiEvent()
}