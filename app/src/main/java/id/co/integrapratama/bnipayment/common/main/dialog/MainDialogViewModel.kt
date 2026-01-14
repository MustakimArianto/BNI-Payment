package id.co.integrapratama.bnipayment.common.main.dialog

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainDialogViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainDialogUiState())
    val uiState: StateFlow<MainDialogUiState> = _uiState.asStateFlow()

    fun onEvent(event: MainDialogUiEvent) {
        when (event) {
            is MainDialogUiEvent.ShowDialog -> {
                _uiState.update {
                    it.copy(
                        contentInjector = event.contentInjector
                    )
                }
            }
            is MainDialogUiEvent.DismissDialog -> {
                _uiState.update {
                    it.copy(
                        contentInjector = null
                    )
                }
            }
        }
    }
}