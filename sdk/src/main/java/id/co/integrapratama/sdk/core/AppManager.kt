package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit

class AppManager(
    private val sharedPrefs: SharedPreferences
) {
    companion object {
        private const val FIRST_INSTALL_KEY = "FIRST_INSTALL_KEY"
        private const val CHECK_DEFAULT_MENU_KEY = "CHECK_DEFAULT_MENU_KEY"
    }

    fun isFirstInstall(): Boolean {
        return sharedPrefs.getBoolean(FIRST_INSTALL_KEY, true)
    }

    fun updateFirstInstall() {
        sharedPrefs.edit { putBoolean(FIRST_INSTALL_KEY, false) }
    }

    fun isCheckDefaultMenu(): Boolean {
        return sharedPrefs.getBoolean(CHECK_DEFAULT_MENU_KEY, true)
    }

    fun updateCheckDefaultMenu() {
        sharedPrefs.edit {
            putBoolean(CHECK_DEFAULT_MENU_KEY, false)
        }
    }
}