package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class LogonManager(
    private val sharedPrefs: SharedPreferences
) {
    private val _isLoggedInToday = MutableStateFlow(isAlreadyLogonToday())
    val isLoggedInToday: StateFlow<Boolean> = _isLoggedInToday

    companion object {
        private const val LAST_LOGON_KEY = "LAST_LOGON_KEY"
    }

    fun getLastLogon(): Int {
        return sharedPrefs.getInt(LAST_LOGON_KEY, -1)
    }

    fun updateLastLogon() {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        sharedPrefs.edit().putInt(LAST_LOGON_KEY, today).apply()

        _isLoggedInToday.value = true
    }

    fun isAlreadyLogonToday(): Boolean {
        return Calendar.getInstance()
            .get(Calendar.DAY_OF_YEAR) == sharedPrefs.getInt(LAST_LOGON_KEY, -1)
    }
}