package id.co.integrapratama.sdk.feature_settlement.domain

import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SettlementRepository {
    fun getTotalSettlementSummary(): Flow<Resource<TotalSettlementSummaryModel>>

    fun postSettlement(): Flow<Resource<ByteArray>>

    fun postBatchUpload(): Flow<Resource<ByteArray>>

    fun printSettlement(): Flow<Resource<Unit>>
}