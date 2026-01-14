package id.co.integrapratama.sdk.core

import id.co.integrapratama.sdk.core.data.local.AppDatabase

class TransactionManager(
    private val appDatabase: AppDatabase
) {

    suspend fun isTerminalHasTransaction(): Boolean {
        val cardDao = appDatabase.cardTransactionDao()
        val transactionData = cardDao.getAllTrxData()
        return transactionData.isNotEmpty()
    }
}