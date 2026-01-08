package id.co.integrapratama.bnipayment.feature_installment

import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPeriodModel
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPlanModel
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOfflinePinAction
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource

data class InstallmentUiState(
    // Transaction data
    val title: String = "Sale",
    val amount: String = "",
    val cardNumber: String = "",
    val maskedCardNumber: String = "",
    val cardType: Int? = null,
    val cvmMethod: Int? = null,
    val cardReadOutput: CardReadOutput? = null,
    val pin: String = "",
    val pinBlock: String = "",
    val transactionDateTime: String = "",

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
    val isFinishedReadCard: Boolean = false,
    val isTransactionFinished: Boolean = false,

    // UI feedback
    val statusMessage: String = "",
    val presentCardAgainMessage: String = "",
    val errorMessage: String = "",
    val dialogLoadingMessage: String = "",

    // Installment Period and Plan List, and Installment Period and Plan selection state
    val installmentPlanListResource: Resource<List<InstallmentPlanModel>>? = null,
    val installmentPeriodListResource: Resource<List<InstallmentPeriodModel>>? = null,
    val selectedInstallmentPlan: InstallmentPlanModel? = null,
    val selectedInstallmentPeriod: InstallmentPeriodModel? = null,
)