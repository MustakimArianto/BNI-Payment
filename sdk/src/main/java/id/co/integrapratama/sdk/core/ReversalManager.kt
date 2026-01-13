package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit

class ReversalManager(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val SALE_REVERSAL_KEY = "SALE_REVERSAL_KEY"
        private const val VOID_REVERSAL_KEY = "VOID_REVERSAL_KEY"
    }

    fun getSaleReversal(): String? {
        return sharedPreferences.getString(SALE_REVERSAL_KEY, null)
    }

    fun saveSaleReversal(data: String) {
        sharedPreferences.edit { putString(SALE_REVERSAL_KEY, data) }
    }

    fun clearSaleReversal() {
        sharedPreferences.edit { remove(SALE_REVERSAL_KEY) }
    }

    fun getVoidReversal(): String? {
        return sharedPreferences.getString(VOID_REVERSAL_KEY, null)
    }

    fun saveVoidReversal(data: String) {
        sharedPreferences.edit { putString(VOID_REVERSAL_KEY, data) }
    }

    fun clearVoidReversal(data: String) {
        sharedPreferences.edit { remove(VOID_REVERSAL_KEY) }
    }
}