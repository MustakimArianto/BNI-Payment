package id.co.integrapratama.sdk.feature_installment.domain

import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.integrapratama.sdk.feature_sale.domain.TransactionRecord
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface InstallmentRepository {
    suspend fun getInstallmentPeriodList(): Flow<Resource<List<InstallmentPeriodModel>>>

    suspend fun getInstallmentPlanList(): Flow<Resource<List<InstallmentPlanModel>>>

    suspend fun postInstallmentTransaction(
        cardClassification: CardClassification,
        request: CardReadOutput,
        transactionDateTime: String
    ): Flow<Resource<ByteArray>>

    suspend fun insertCardTransactionToDatabase(transactionRecord: TransactionRecord): Flow<Resource<Unit>>

    suspend fun printReceiptBasedLastTraceNo(): Flow<Resource<Unit>>

    suspend fun printReceiptBasedTraceNo(
        traceNo: String
    ): Flow<Resource<Unit>>
}