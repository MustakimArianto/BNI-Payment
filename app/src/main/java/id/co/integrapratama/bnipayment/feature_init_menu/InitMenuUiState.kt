package id.co.integrapratama.bnipayment.feature_init_menu

data class InitMenuUiState(
    val currentStep: Int = 0,
    val maxStep: Int = 3,
    val isInitSuccess: Boolean = false,
    val isInitFailed: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,

    val loadingMessage: String = "",
    val resultMessage: String = "",
)