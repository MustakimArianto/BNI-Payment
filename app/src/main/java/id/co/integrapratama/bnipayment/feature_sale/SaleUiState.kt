package id.co.integrapratama.bnipayment.feature_sale

import id.co.integrapratama.sdk.feature_bin_range.domain.BinType
import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOfflinePinAction
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import java.util.Date

data class SaleUiState(
    // Transaction data
    var isContactless: Boolean = false,
    var amount: String = "",
    var tip: String = "",
    var cardNumber: String = "",
    var maskedCardNumber: String = "",
    var cardType: Int? = null,
    var cvmMethod: Int? = null,
    var cardReadOutput: CardReadOutput? = null,
    var pin: String = "",
    var pinBlock: String = "",
    var transactionDateTime: Date = Date(),
    var binType: BinType = BinType.UNKNOWN,

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
    // Computed property that always reflects current value
    val title: String
        get() = "Sale ${if (isContactless) "Contactless" else ""}"
    val cardOption: CardOption
        get() = CardOption(
            supportContactless = isContactless,
            supportDip = !isContactless,
            supportSwipe = false
        )
}