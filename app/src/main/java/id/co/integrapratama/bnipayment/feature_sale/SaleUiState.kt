package id.co.integrapratama.bnipayment.feature_sale

data class SaleUiState(
    // Transaction data
    var amount: String = "",
    val cardNumber: String = "",
    val maskedCardNumber: String = "",

    // Process / loading flags
    val isLoading: Boolean = false,
    val isReadingCard: Boolean = false,
    val isPrinting: Boolean = false,

    // Completion & result states
    val isFinishedReadCard: Boolean = false,
    val isTransactionSuccess: Boolean = false,

    // Error States
    val errorMessage: String = ""
)