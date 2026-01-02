package id.co.integrapratama.bnipayment.feature_sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.bnipayment.common.maskCardNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(SaleUiState())
    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    fun onEvent(event: SaleUiEvent) {
        when (event) {
            is SaleUiEvent.OnAmountChange -> {
                _uiState.value = _uiState.value.copy(amount = event.amount)
            }
        }
    }

    fun readCard() {
        viewModelScope.launch {
            delay(1500)
            _uiState.update { it.copy(isReadingCard = true) }
            delay(1500)
            val cardNumber = "4105050000045287"

            _uiState.update {
                it.copy(
                    cardNumber = cardNumber,
                    maskedCardNumber = maskCardNumber(cardNumber),
                    isReadingCard = false,
                    isFinishedReadCard = true,
                    isTransactionSuccess = true
                )
            }
        }
    }

    fun printReceipt() {

    }
}