package id.co.integrapratama.bnipayment.feature_sale

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleContactlessDialog(
    title: String,
    message: String,
) {
    BasicAlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                    VerticalSpacer(SpacerSize.MEDIUM)
                    Image(
                        painter = painterResource(R.drawable.ic_tap_card), contentDescription = null
                    )
                    VerticalSpacer(SpacerSize.MEDIUM)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = message,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}