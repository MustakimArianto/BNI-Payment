package id.co.integrapratama.bnipayment.feature_mini_atm.balance_info

import id.co.integrapratama.sdk.feature_bin_range.domain.BinType
import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOfflinePinAction
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import java.util.Date

data class MiniATMBalanceInfoUiState(
    val title: String = "Info Saldo",

    // Transaction data
    var balance: String = "",
    var isContactless: Boolean = true,
    var cardReadOutput: CardReadOutput? = null,
    var pin: String = "",
    var pinBlock: String = "",
    var transactionDateTime: Date = Date(),
    var binType: BinType = BinType.UNKNOWN,
    var cardNumber: String = "",
    var maskedCardNumber: String = "",
    var cardOption: CardOption = CardOption(
        supportContactless = true,
        supportDip = true,
        supportSwipe = false
    ),

    // Process flags
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

    // UI Feedback
    var loadingMessage: String = "",
    var presentCardAgainMessage: String = "",
    var transactionResultMessage: String = "",
    var errorMessage: String = ""
)