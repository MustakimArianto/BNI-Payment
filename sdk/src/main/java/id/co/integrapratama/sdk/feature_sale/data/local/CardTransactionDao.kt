package id.co.integrapratama.sdk.feature_sale.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import id.co.integrapratama.sdk.core.utils.TransactionType

@Dao
interface CardTransactionDao {
    @Query("SELECT * FROM card_transaction WHERE invoice = :traceNo ORDER BY id desc LIMIT 1")
    suspend fun getTrxDataByTraceNo(
        traceNo: String
    ): CardTransactionEntity?

    @Query("SELECT * FROM card_transaction WHERE txnTypeId IN (:txnTypeId) AND invoice =:traceNo ORDER BY id desc LIMIT 1")
    suspend fun getTrxDataByTypeIdAndTraceNumber(
        traceNo: String,
        vararg txnTypeId: String,
    ): CardTransactionEntity?

    @Query("SELECT * FROM card_transaction")
    suspend fun getAllTrxData(): List<CardTransactionEntity>

    @Query("SELECT * FROM card_transaction WHERE substr(invoiceDate, 1, 6) = :date")
    suspend fun getTransactionsByDate(date: String): List<CardTransactionEntity>

    @Query("SELECT * FROM card_transaction where txnTypeId = '495' OR txnTypeId = :txnTypeId")
    suspend fun getAllTrxDataInstallment(txnTypeId: String = TransactionType.VOID.name): List<CardTransactionEntity>

    @Query("SELECT * FROM card_prev_transaction")
    suspend fun getAllTrxPrevData(): List<CardPrevTransactionEntity>

    @Query("SELECT DISTINCT acquiringBank FROM card_transaction")
    suspend fun getListAcquiryBank(): List<String>

    @Query("SELECT DISTINCT acquiringBank FROM card_prev_transaction")
    suspend fun getListAcquiryBankPrev(): List<String>

    @Query("SELECT * FROM card_transaction LIMIT 1")
    suspend fun getOneTransaction(): CardTransactionEntity?

    @Query("SELECT * FROM card_transaction WHERE txnTypeId = :txnTypeId")
    suspend fun getAllTrxDataByTxnTypeId(txnTypeId: String): List<CardTransactionEntity>

    @Query("SELECT * FROM card_transaction WHERE txnTypeId = :txnTypeId ORDER BY id DESC LIMIT 1")
    suspend fun getLastTxnDataByTxnTypeId(txnTypeId: String): CardTransactionEntity?

    @Query("SELECT * FROM card_transaction ORDER BY id DESC LIMIT 1")
    suspend fun getLastTxnData(): CardTransactionEntity?

    @Query("SELECT * FROM card_transaction WHERE invoice =:invoice AND txnTypeId = :txnTypeId ORDER BY id DESC")
    suspend fun getVoidByTraceNumber(invoice: String, txnTypeId: String = TransactionType.VOID.name): CardTransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CardTransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllToPrevious(item: List<CardPrevTransactionEntity>)

    @Query("DELETE FROM card_transaction")
    suspend fun deleteAllTransactions()

    @Query("DELETE FROM card_prev_transaction")
    suspend fun deleteAllPreviousTransactions()


    @Transaction
    suspend fun moveAllTrxToPreviousBatch() {
        deleteAllPreviousTransactions()
        val allTrxData = getAllTrxData().map { it.toPreviousBatch() }
        insertAllToPrevious(allTrxData)
        deleteAllTransactions()
    }
}