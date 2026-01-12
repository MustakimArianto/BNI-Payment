package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit

class TerminalConfigManager(
    private val sharedPrefs: SharedPreferences
) {
    companion object {
        private const val TID_KEY = "TID_KEY"
        private const val MID_KEY = "MID_KEY"
    }

    fun getTid(): String? {
        return sharedPrefs.getString(TID_KEY, null)
    }

    fun saveTid(tid: String) {
        sharedPrefs.edit { putString(TID_KEY, tid) }
    }

    fun getMid(): String? {
        return sharedPrefs.getString(MID_KEY, null)
    }

    fun saveMid(mid: String) {
        sharedPrefs.edit { putString(MID_KEY, mid) }
    }
}