package id.co.integrapratama.sdk.core

import android.content.SharedPreferences

class TraceNumberManager(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val TRACE_NUMBER_KEY = "TRACE_NUMBER_KEY"
    }

    fun getCurrentTraceNo(): Long {
        return sharedPreferences.getLong(TRACE_NUMBER_KEY, 1)
    }

    fun increment() {
        sharedPreferences.edit().putLong(TRACE_NUMBER_KEY, getCurrentTraceNo() + 1).apply()
    }

    fun saveTraceNo(traceNo: Long) {
        sharedPreferences.edit().putLong(TRACE_NUMBER_KEY, traceNo).apply()
    }

    fun reset() {
        sharedPreferences.edit().putLong(TRACE_NUMBER_KEY, 1).apply()
    }
}