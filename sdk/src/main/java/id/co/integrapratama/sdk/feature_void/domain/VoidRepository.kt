package id.co.integrapratama.sdk.feature_void.domain

import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface VoidRepository {
    suspend fun postVoidTransaction(
        voidRequestModel: VoidRequestModel
    ): Flow<Resource<ByteArray>>

    suspend fun checkVoidTransaction(traceNo: String): Flow<Resource<VoidRequestModel>>

    suspend fun updateVoidTransaction(traceNo: String): Flow<Resource<Unit>>

    suspend fun printVoidBasedTraceNo(traceNo: String): Flow<Resource<Unit>>
}