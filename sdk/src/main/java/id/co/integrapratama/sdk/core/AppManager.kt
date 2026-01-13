package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit

class AppManager(
    private val sharedPrefs: SharedPreferences
) {
    companion object {
        private const val FIRST_INSTALL_KEY = "FIRST_INSTALL_KEY"
    }

    fun isFirstInstall(): Boolean {
        return sharedPrefs.getBoolean(FIRST_INSTALL_KEY, true)
    }

    fun updateFirstInstall() {
        sharedPrefs.edit { putBoolean(FIRST_INSTALL_KEY, false) }
    }
}