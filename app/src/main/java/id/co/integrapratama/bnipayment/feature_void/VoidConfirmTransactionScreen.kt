package id.co.integrapratama.bnipayment.feature_void

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.common.model.SubmitSummary
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.sdk.core.utils.StringUtil
import id.co.integrapratama.sdk.feature_void.domain.VoidRequestModel

@Composable
fun VoidConfirmTransactionScreen(
    voidRequestModel: VoidRequestModel,
    onSubmitVoid: () -> Unit,
    onBackClick: () -> Unit
) {
    val submitSummaryList: List<SubmitSummary> = listOf(
        SubmitSummary(
            text = "Waktu Dibuat",
            value = voidRequestModel.invoiceDate
        ),
        SubmitSummary(
            text = "No. Tracing",
            value = voidRequestModel.invoice
        ),
        SubmitSummary(
            text = "No. Kartu",
            value = voidRequestModel.maskedCardNo ?: ""
        ),
        SubmitSummary(
            text = "Nilai Transaksi",
            value = StringUtil.formatRupiahCurrency(voidRequestModel.amount.toString())
        ),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(title = "Void")
        VerticalSpacer(SpacerSize.X_LARGE)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item(1) {
                    for ((i, submitSummary) in submitSummaryList.withIndex()) {
                        if (i > 0) {
                            VoidSummaryDivider()
                        }
                        VoidSummary(
                            text = submitSummary.text,
                            value = submitSummary.value
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = "Gagalkan Transaksi",
                onClick = onSubmitVoid
            )
        }
    }
}


@Composable
private fun VoidSummary(
    text: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = value
        )
    }
}

@Composable
private fun VoidSummaryDivider() {
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        Divider(
            color = Color(179, 179, 179, 255)
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}