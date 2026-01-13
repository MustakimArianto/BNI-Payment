package id.co.integrapratama.sdk.feature_mini_atm.balance_info.domain

import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface MiniATMBalanceInfoRepository {
    suspend fun postBalanceInfoTransaction(
        isFromSaving: Boolean,
        request: CardReadOutput,
        transactionDateTime: Date
    ): Flow<Resource<ByteArray>>

    fun preparePrintingData(
        balance: String, request: CardReadOutput, transactionDateTime: Date
    ): PrintBasedOnTemplateParameter

    suspend fun printCurrentReceipt(
        template: PrintBasedOnTemplateParameter
    ): Flow<Resource<Unit>>
}