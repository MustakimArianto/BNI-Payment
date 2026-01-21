package id.co.integrapratama.bnipayment.feature_void

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.ImageAidName
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryTextField
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.LightGray
import id.co.integrapratama.bnipayment.ui.theme.PrimaryVariantColor
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.StringUtil
import id.co.integrapratama.sdk.feature_void.domain.ListVoidModel

@Composable
fun VoidInputTraceNoScreen(
    title: String,
    traceNo: String = "", transactionList: List<ListVoidModel>?,
    onTraceNoChanged: (String) -> Unit,
    onTraceClick: () -> Unit, onTransactionItemClick: ((ListVoidModel) -> Unit)? = null
) {
    Column(Modifier.background(Color.White), verticalArrangement = Arrangement.Center) {
        TopBar(title = title)
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PrimaryTextField(
                    modifier = Modifier
                        .background(Color.White)
                        .weight(1f)
                        .height(56.dp),
                    value = traceNo,
                    onValueChange = onTraceNoChanged, roundedCornerShape = RoundedCornerShape(30.dp)
                )
                PrimaryButton(
                    modifier = Modifier.height(56.dp),
                    text = stringResource(R.string.text_trace),
                    onClick = onTraceClick,
                    roundedCornerShape = RoundedCornerShape(30.dp),
                    isMaxWidth = false
                )
            }
        }

        VerticalSpacer(SpacerSize.MEDIUM)

        if (transactionList?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transactionList) { transaction ->
                    TransactionListItem(
                        transaction = transaction,
                        onClick = { onTransactionItemClick?.invoke(transaction) })

                    HorizontalDivider(color = LightGray)
                }
            }
        }
    }
}

@Composable
private fun TransactionListItem(
    transaction: ListVoidModel, onClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trace No. ${transaction.invoice}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            ImageAidName(transaction.aidName)
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = DateUtils.getVoidListTransactionDateTime(transaction.invoiceDate) + " WIB",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = StringUtil.formatRupiahCurrency(transaction.amount.toString()),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryVariantColor
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reff No. ${transaction.refNo}", fontSize = 12.sp, color = Color.Gray
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = transaction.cardClassification,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(text = "•", fontSize = 32.sp, color = LightGray)
                Text(
                    text = transaction.transactionScope,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    }
}
