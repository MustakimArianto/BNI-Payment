package id.co.integrapratama.sdk.feature_settlement.domain

import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SettlementRepository {
    fun getTotalSettlementSummary(): Flow<Resource<TotalSettlementSummaryModel>>

    fun postSettlementAndBatchUpload(): Flow<Resource<PostSettlementAndBatchUploadResponseModel>>

    fun printSettlement(settlementPrintBasedOnTemplateParameter: PrintBasedOnTemplateParameter): Flow<Resource<Unit>>
}