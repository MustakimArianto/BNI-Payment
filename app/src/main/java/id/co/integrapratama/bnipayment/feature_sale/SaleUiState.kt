package id.co.integrapratama.bnipayment.feature_sale

import id.co.integrapratama.sdk.feature_sale.data.dto.SaleResponseDto
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput

data class SaleUiState(
    // Transaction data
    val amount: String = "",
    val cardNumber: String = "",
    val maskedCardNumber: String = "",
    val cardType: Int? = null,
    val cvmMethod: Int? = null,
    val cardReadOutput: CardReadOutput? = null,

    // Process flags
    val isLoading: Boolean = false,
    val isReadingCard: Boolean = false,
    val isProcessing: Boolean = false,

    // Completion states
    val isFinishedReadCard: Boolean = false,
    val isTransactionSuccess: Boolean = false,

    // UI feedback
    val statusMessage: String = "",
    val errorMessage: String = "",
)