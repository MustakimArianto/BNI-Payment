package id.co.integrapratama.bnipayment.feature_admin

data class AdminUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,

    val loadingMessage: String = "",
    val resultMessage: String = "",
)