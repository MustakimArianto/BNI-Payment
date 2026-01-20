package id.co.integrapratama.bnipayment.feature_installment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.common.ui_component.InputAmountTextField
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.ResourceImplementer
import id.co.integrapratama.bnipayment.common.ui_component.SelectionButton
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPeriodModel
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPlanModel
import id.co.payment2go.terminalsdkhelper.core.util.Resource

@Composable
fun InstallmentInputAmountScreen(
    amount: String = "",
    onAmountChanged: (String) -> Unit,
    onNextClick: () -> Unit,
    onNavigationBack: () -> Unit,
    installmentPlanListResource: Resource<List<InstallmentPlanModel>>?,
    selectedInstallmentPlan: InstallmentPlanModel?,
    onSelectInstallmentPlan: (InstallmentPlanModel) -> Unit,
    installmentPeriodListResource: Resource<List<InstallmentPeriodModel>>?,
    selectedInstallmentPeriod: InstallmentPeriodModel?,
    onSelectInstallmentPeriod: (InstallmentPeriodModel) -> Unit,
) {
    Column(
        Modifier
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        TopBar(title = "Installment")
        VerticalSpacer(SpacerSize.X_LARGE)

        Text(
            text = "Pilih Plan",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        VerticalSpacer(SpacerSize.MEDIUM)
        ResourceImplementer(
            resource = installmentPlanListResource,
            onSuccess = {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    it!!.forEach { installmentPlan ->
                        SelectionButton(
                            text = installmentPlan.name,
                            selected = selectedInstallmentPlan?.id == installmentPlan.id,
                            onClick = {
                                onSelectInstallmentPlan(installmentPlan)
                            }
                        )
                    }
                }
            }
        )

        VerticalSpacer(SpacerSize.X_LARGE)
        Text(
            text = "Periode Cicilan",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        VerticalSpacer(SpacerSize.MEDIUM)
        ResourceImplementer(
            resource = installmentPeriodListResource,
            onSuccess = {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    it!!.forEach { installmentPeriod ->
                        SelectionButton(
                            text = installmentPeriod.name,
                            selected = selectedInstallmentPeriod?.id == installmentPeriod.id,
                            onClick = {
                                onSelectInstallmentPeriod(installmentPeriod)
                            }
                        )
                    }
                }
            }
        )

        VerticalSpacer(SpacerSize.X_LARGE)

        InputAmountTextField(
            amount = amount,
            onAmountChanged = onAmountChanged,
        )

        Box(Modifier.fillMaxSize()) {
            PrimaryButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "Lanjut",
                onClick = onNextClick
            )
        }
    }
}