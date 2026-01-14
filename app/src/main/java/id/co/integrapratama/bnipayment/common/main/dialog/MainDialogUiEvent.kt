package id.co.integrapratama.bnipayment.common.main.dialog

sealed class MainDialogUiEvent {
    data class ShowDialog(
        val contentInjector: MainDialogContentInjector
    ): MainDialogUiEvent()

    data object DismissDialog: MainDialogUiEvent()
}