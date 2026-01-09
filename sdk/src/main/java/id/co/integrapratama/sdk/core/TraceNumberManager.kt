package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit

class TraceNumberManager(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val TRACE_NUMBER_KEY = "TRACE_NUMBER_KEY"
        private const val LAST_TRACE_NUMBER_KEY = "LAST_TRACE_NUMBER_KEY"
    }

    fun getCurrentTraceNo(): Long {
        return sharedPreferences.getLong(TRACE_NUMBER_KEY, 1)
    }

    fun getCurrentLastTraceNo(): Long {
        return sharedPreferences.getLong(LAST_TRACE_NUMBER_KEY, 1)
    }

    fun increment() {
        sharedPreferences.edit {
            putLong(LAST_TRACE_NUMBER_KEY, getCurrentTraceNo())
            putLong(TRACE_NUMBER_KEY, getCurrentTraceNo() + 1)
        }
    }

    fun saveTraceNo(traceNo: Long) {
        sharedPreferences.edit {
            putLong(TRACE_NUMBER_KEY, traceNo)
        }
    }

    fun saveLastTraceNo(lastTraceNo: Long) {
        sharedPreferences.edit {
            putLong(LAST_TRACE_NUMBER_KEY, lastTraceNo)
        }
    }

    fun reset() {
        sharedPreferences.edit {
            putLong(LAST_TRACE_NUMBER_KEY, 1)
            putLong(TRACE_NUMBER_KEY, 1)
        }
    }
}