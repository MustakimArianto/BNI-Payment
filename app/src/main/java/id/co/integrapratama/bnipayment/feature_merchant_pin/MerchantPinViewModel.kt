package id.co.integrapratama.bnipayment.feature_merchant_pin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MerchantPinViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MerchantPinUiState())
    val uiState: StateFlow<MerchantPinUiState> = _uiState.asStateFlow()

    fun onEvent(event: MerchantPinUiEvent) {
        when (event) {
            is MerchantPinUiEvent.ChangeMerchantPin -> {
                _uiState.update {
                    it.copy(
                        merchantPin = event.pin
                    )
                }
            }

            is MerchantPinUiEvent.SubmitMerchantPin -> {
                submitMerchantPin()
            }
        }
    }

    private fun submitMerchantPin() {
        val merchantPin = uiState.value.merchantPin
        _uiState.update {
            it.copy(
                loadingMessage = "",
                checkingMerchantPinStatus = if (merchantPin == "1234") 1 else 0
            )
        }
    }
}