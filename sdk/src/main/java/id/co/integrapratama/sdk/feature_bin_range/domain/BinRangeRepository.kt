package id.co.integrapratama.sdk.feature_bin_range.domain

import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface BinRangeRepository {
    suspend fun getBinType(cardNumber: String): Flow<Resource<BinType>>

    fun classifyCard(binType: BinType): CardClassification
}