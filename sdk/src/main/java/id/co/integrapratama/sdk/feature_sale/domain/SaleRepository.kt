package id.co.integrapratama.sdk.feature_sale.domain

import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface SaleRepository {
    suspend fun postSaleTransaction(
        cardClassification: CardClassification,
        request: CardReadOutput,
        transactionDateTime: Date
    ): Flow<Resource<ByteArray>>

    suspend fun insertCardTransactionToDatabase(transactionRecord: TransactionRecord): Flow<Resource<Unit>>

    fun preparePrintingData(
        request: CardReadOutput, transactionDateTime: Date
    ): PrintBasedOnTemplateParameter

    suspend fun printReceiptBasedLastTraceNo(): Flow<Resource<Unit>>

    suspend fun printReceiptBasedTraceNo(
        traceNo: String
    ): Flow<Resource<Unit>>

    suspend fun postSaleReversal(): Flow<Resource<ByteArray>>
}