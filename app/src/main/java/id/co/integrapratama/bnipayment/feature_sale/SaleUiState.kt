package id.co.integrapratama.bnipayment.feature_sale

import id.co.integrapratama.sdk.feature_bin_range.domain.BinType
import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput

data class SaleUiState(
    // Transaction data
    var isContactless: Boolean = false,
    var amount: String = "",
    var cardNumber: String = "",
    var maskedCardNumber: String = "",
    var cardType: Int? = null,
    var cvmMethod: Int? = null,
    var cardReadOutput: CardReadOutput? = null,
    var pin: String = "",
    var pinBlock: String = "",
    var transactionDateTime: String = "",
    var binType: BinType = BinType.UNKNOWN,

    // Process flags
    var isLoading: Boolean = false,
    var isReadingCard: Boolean = false,
    var isProcessing: Boolean = false,
    var isCardConfirmed: Boolean = false,
    var isShowPinpad: Boolean = false,
    var onInsertOnlinePinAction: OnInsertOnlinePinAction? = null,
    var isPhysicalKeyboard: Boolean = false,

    // Completion states
    var isFinishedReadCard: Boolean = false,
    var isTransactionFinished: Boolean = false,

    // UI feedback
    var loadingMessage: String = "",
    var presentCardAgainMessage: String = "",
    var errorMessage: String = "",
    var transactionResultMessage: String = "",
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