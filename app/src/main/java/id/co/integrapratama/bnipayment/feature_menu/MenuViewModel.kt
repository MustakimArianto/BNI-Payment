package id.co.integrapratama.bnipayment.feature_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.sdk.core.LogonManager
import id.co.integrapratama.sdk.core.TransactionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    val logonManager: LogonManager,
    val transactionManager: TransactionManager
) : ViewModel() {

    private val _isHomeActive = MutableStateFlow(false)
    val isHomeActive: StateFlow<Boolean> = _isHomeActive

    init {
        observeHomeAvailability()
    }

    private fun observeHomeAvailability() {
        viewModelScope.launch {
            logonManager.isLoggedInToday.collect { loggedIn ->
                val hasTransaction = transactionManager.isTerminalHasTransaction()

                // Home OFF only when NOT logged in AND NO transaction
                _isHomeActive.value = loggedIn || hasTransaction
            }
        }
    }
}