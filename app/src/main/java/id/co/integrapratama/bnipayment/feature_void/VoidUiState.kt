package id.co.integrapratama.bnipayment.feature_void

import id.co.integrapratama.sdk.feature_bin_range.domain.BinType
import id.co.integrapratama.sdk.feature_void.domain.VoidRequestModel
import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOfflinePinAction
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import java.util.Date

data class VoidUiState(
    // Transaction data
    var isContactless: Boolean = false,
    var traceNo: String = "",
    var cardNumber: String = "",
    var maskedCardNumber: String = "",
    var cardType: Int? = null,
    var cvmMethod: Int? = null,
    var cardReadOutput: CardReadOutput? = null,
    var pin: String = "",
    var pinBlock: String = "",
    var transactionDateTime: Date = Date(),
    var binType: BinType = BinType.UNKNOWN,
    val voidRequestModel: VoidRequestModel? = null,

    // Process flags
    val isLoading: Boolean = false,
    val isReadingCard: Boolean = false,
    val isProcessing: Boolean = false,
    val isCardConfirmed: Boolean = false,
    val isShowPinpad: Boolean = false,
    val isShowOfflinePinpad: Boolean = false,
    val onInsertOnlinePinAction: OnInsertOnlinePinAction? = null,
    val onInsertOfflinePinAction: OnInsertOfflinePinAction? = null,
    val isPhysicalKeyboard: Boolean = false,
    val hasConfirmVoid: Boolean = false,

    // Completion states
    var isFinishedReadCard: Boolean = false,
    var isTransactionFinished: Boolean = false,

    // UI feedback
    var loadingMessage: String = "",
    var presentCardAgainMessage: String = "",
    var errorMessage: String = "",
    var transactionResultMessage: String = "",
    var reversalResultMessage: String = ""
) {
    val title: String = "Void"
}