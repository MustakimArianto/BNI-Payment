package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit

class HostUrlManager(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val HOST_URL_KEY = "HOST_URL_KEY"
    }

    fun getCurrentHostUrl(): String {
        return sharedPreferences.getString(HOST_URL_KEY, "10.86.223.143") ?: ""
    }

    fun saveHostUrl(hostUrl: String) {
        sharedPreferences.edit { putString(HOST_URL_KEY, hostUrl) }
    }
}