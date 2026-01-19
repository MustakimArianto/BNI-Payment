package id.co.integrapratama.bnipayment.common.main.pinpad

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainPinpadViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainPinpadUiState())
    val uiState: StateFlow<MainPinpadUiState> = _uiState.asStateFlow()

    fun onEvent(event: MainPinpadUiEvent) {
        when (event) {
            is MainPinpadUiEvent.ShowPinpad -> {
                _uiState.update {
                    it.copy(
                        onInsertOnlinePinAction = event.onInsertOnlinePinAction
                    )
                }
            }
            is MainPinpadUiEvent.ShowOfflinePinpad -> {
                _uiState.update {
                    it.copy(
                        onInsertOfflinePinAction = event.onInsertOfflinePinAction
                    )
                }
            }
        }
    }
}