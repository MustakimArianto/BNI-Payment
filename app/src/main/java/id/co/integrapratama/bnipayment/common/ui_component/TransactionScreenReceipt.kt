package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.spacer.HorizontalSpacer
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.StringUtil
import java.util.Date

@Composable
fun TransactionScreenReceipt(
    title: String,
    amount: String,
    dateTime: Date,
    content: @Composable () -> Unit,
    onPrint: () -> Unit,
    onEmail: () -> Unit,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    painter = painterResource(R.drawable.ic_topbar_background),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 32.dp, start = 16.dp),
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 52.dp),
                    painter = painterResource(R.drawable.ic_transaction_success),
                    contentDescription = null
                )
            }

            VerticalSpacer(SpacerSize.MEDIUM)
            Text(text = stringResource(R.string.text_transaction_approved))
            Text(
                StringUtil.formatRupiahCurrency(amount),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(DateUtils.getScreenReceiptTransactionDate(date = dateTime))
                Text("-")
                Text(DateUtils.getScreenReceiptTransactionTime(date = dateTime))
            }
            VerticalSpacer(SpacerSize.X_LARGE)
            content()

            // Add bottom padding to prevent content from being hidden behind buttons
            VerticalSpacer(SpacerSize.MEDIUM)
        }

        // Fixed bottom buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.text_print),
                    icon = R.drawable.ic_print_blue,
                    isOutline = true,
                    onClick = onPrint
                )
                HorizontalSpacer(SpacerSize.MEDIUM)
                SecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.text_email),
                    icon = R.drawable.ic_email_blue,
                    isOutline = true,
                    onClick = onEmail
                )
            }

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.text_back_to_home),
                onClick = onBackToHome
            )
        }
    }
}