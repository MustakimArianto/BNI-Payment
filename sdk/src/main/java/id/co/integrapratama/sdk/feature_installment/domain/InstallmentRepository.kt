package id.co.integrapratama.sdk.feature_installment.domain

import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface InstallmentRepository {
    suspend fun getInstallmentPeriodList(): Flow<Resource<List<InstallmentPeriodModel>>>

    suspend fun getInstallmentPlanList(): Flow<Resource<List<InstallmentPlanModel>>>

    suspend fun postInstallmentTransaction(
        isFromSaving: Boolean,
        request: CardReadOutput,
        transactionDateTime: String
    ): Flow<Resource<ByteArray>>
}