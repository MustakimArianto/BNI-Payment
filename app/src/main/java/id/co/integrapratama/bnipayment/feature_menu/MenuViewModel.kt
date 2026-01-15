package id.co.integrapratama.bnipayment.feature_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.sdk.core.AppManager
import id.co.integrapratama.sdk.core.LogonManager
import id.co.integrapratama.sdk.core.TransactionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    val appManager: AppManager,
    val logonManager: LogonManager,
    val transactionManager: TransactionManager
) : ViewModel() {

    private val _isHomeActive = MutableStateFlow(false)
    val isHomeActive: StateFlow<Boolean> = _isHomeActive

    private val _isCheckDefaultMenu = MutableStateFlow(appManager.isCheckDefaultMenu())
    val isCheckDefaultMenu: StateFlow<Boolean> = _isCheckDefaultMenu.asStateFlow()

    init {
        observeHomeAvailability()
    }

    fun onEvent(event: MenuEvent) {
        if (event == MenuEvent.DisableMenuChecking) {
            appManager.updateCheckDefaultMenu()
            _isCheckDefaultMenu.value = false
        }
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