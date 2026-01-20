package id.co.integrapratama.sdk.feature_settlement.domain

import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter

class PostSettlementAndBatchUploadResponseModel(
    val isoResponse: ByteArray,
    val settlementPrintBasedOnTemplateParameter: PrintBasedOnTemplateParameter
)