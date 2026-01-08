package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit

class TerminalBatchManager(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val TERMINAL_BATCH_KEY = "TERMINAL_BATCH_KEY"
        private const val LAST_TERMINAL_BATCH_KEY = "LAST_TERMINAL_BATCH_KEY"
    }

    fun getCurrentBatch(): Long {
        return sharedPreferences.getLong(TERMINAL_BATCH_KEY, 1)
    }

    fun getCurrentLastBatch(): Long {
        return sharedPreferences.getLong(LAST_TERMINAL_BATCH_KEY, 1)
    }

    fun incrementBatch() {
        sharedPreferences.edit {
            putLong(LAST_TERMINAL_BATCH_KEY, getCurrentBatch())
            putLong(TERMINAL_BATCH_KEY, getCurrentBatch() + 1)
        }
    }

    fun saveBatch(tbid: Long) {
        sharedPreferences.edit { putLong(TERMINAL_BATCH_KEY, tbid) }
    }

    fun saveLastBatch(lastTbid: Long) {
        sharedPreferences.edit { putLong(LAST_TERMINAL_BATCH_KEY, lastTbid) }
    }

    fun reset() {
        sharedPreferences.edit {
            putLong(LAST_TERMINAL_BATCH_KEY, 1)
            putLong(TERMINAL_BATCH_KEY, 1)
        }
    }
}