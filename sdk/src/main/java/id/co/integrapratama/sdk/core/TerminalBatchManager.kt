package id.co.integrapratama.sdk.core

import android.content.SharedPreferences

class TerminalBatchManager(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val TERMINAL_BATCH_KEY = "TERMINAL_BATCH_KEY"
    }

    fun getCurrentBatch(): Long {
        return sharedPreferences.getLong(TERMINAL_BATCH_KEY, 1)
    }

    fun incrementBatch() {
        sharedPreferences.edit().putLong(TERMINAL_BATCH_KEY, getCurrentBatch() + 1).apply()
    }

    fun saveBatch(tbid: Long) {
        if (tbid > getCurrentBatch()) {
            sharedPreferences.edit().putLong(TERMINAL_BATCH_KEY, tbid).apply()
        }
    }
}