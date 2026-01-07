package id.co.integrapratama.sdk.feature_sale.domain

import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    suspend fun postSaleTransaction(isFromSaving: Boolean, request: CardReadOutput, transactionDateTime: String): Flow<Resource<ByteArray>>
}