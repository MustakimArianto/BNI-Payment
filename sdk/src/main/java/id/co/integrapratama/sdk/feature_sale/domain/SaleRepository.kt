package id.co.integrapratama.sdk.feature_sale.domain

import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    suspend fun postSaleTransaction(
        cardClassification: CardClassification,
        request: CardReadOutput,
        transactionDateTime: String
    ): Flow<Resource<ByteArray>>

    suspend fun insertCardTransactionToDatabase(transactionRecord: TransactionRecord): Flow<Resource<Unit>>
}