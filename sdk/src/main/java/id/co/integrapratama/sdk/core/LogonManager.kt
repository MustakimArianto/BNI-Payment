package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.Calendar

class LogonManager(
    private val sharedPrefs: SharedPreferences
) {
    companion object {
        private const val LAST_LOGON_KEY = "LAST_LOGON_KEY"
    }

    fun getLastLogon(): Int {
        return sharedPrefs.getInt(LAST_LOGON_KEY, -1)
    }

    fun updateLastLogon() {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        sharedPrefs.edit { putInt(LAST_LOGON_KEY, dayOfYear) }
    }

    fun isAlreadyLogonToday(): Boolean {
        return Calendar.getInstance()
            .get(Calendar.DAY_OF_YEAR) == sharedPrefs.getInt(LAST_LOGON_KEY, -1)
    }
}