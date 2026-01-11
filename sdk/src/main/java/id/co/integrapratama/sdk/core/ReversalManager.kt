package id.co.integrapratama.sdk.core

import android.content.SharedPreferences

class ReversalManager(
    private val sharedPrefs: SharedPreferences
) {
    companion object {
        private const val SALE_REVERSAL_KEY = "SALE_REVERSAL_KEY"
        private const val VOID_REVERSAL_KEY = "VOID_REVERSAL_KEY"
    }

    fun getSaleReversal(): String? {
        return sharedPrefs.getString(SALE_REVERSAL_KEY, null)
    }

    fun saveSaleReversal(data: String) {
        sharedPrefs.edit().putString(SALE_REVERSAL_KEY, data).apply()
    }

    fun clearSaleReversal() {
        sharedPrefs.edit().remove(SALE_REVERSAL_KEY).apply()
    }

    fun getVoidReversal(): String? {
        return sharedPrefs.getString(VOID_REVERSAL_KEY, null)
    }

    fun setVoidReversal(data: String) {
        sharedPrefs.edit().putString(VOID_REVERSAL_KEY, data).apply()
    }

    fun clearVoidReversal(data: String) {
        sharedPrefs.edit().remove(VOID_REVERSAL_KEY).apply()
    }
}