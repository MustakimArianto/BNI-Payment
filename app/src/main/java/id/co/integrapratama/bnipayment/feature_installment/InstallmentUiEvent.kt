package id.co.integrapratama.bnipayment.feature_installment

import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPeriodModel
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPlanModel

sealed class InstallmentUiEvent {
    data class OnAmountChange(val amount: String) : InstallmentUiEvent()
    data object OnConfirmCard : InstallmentUiEvent()

    data class MappingPinpad(
        val containerInfo: CustomPinpadUiBounds,
        val pinpadMap: List<CustomPinpadUiBounds>,
    ) : InstallmentUiEvent()

    data class MappingOfflinePinpad(
        val containerInfo: CustomPinpadUiBounds,
        val pinpadMap: List<CustomPinpadUiBounds>,
    ) : InstallmentUiEvent()

    data class OnInstallmentPlanSelectionChange(
        val installmentPlanModel: InstallmentPlanModel
    ) : InstallmentUiEvent()

    data class OnInstallmentPeriodSelectionChange(
        val installmentPeriodModel: InstallmentPeriodModel
    ) : InstallmentUiEvent()
}