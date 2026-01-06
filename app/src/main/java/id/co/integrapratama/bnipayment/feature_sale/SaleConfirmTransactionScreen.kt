package id.co.integrapratama.bnipayment.feature_sale

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.SecondaryButton
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.ui.theme.DisabledInputFieldColor
import id.co.integrapratama.bnipayment.ui.theme.TextGray

@Composable
fun SaleConfirmTransactionScreen(
    cardNumber: String,
    onCancel: () -> Unit,
    onNext: () -> Unit,
    onNavigationBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(title = "Sale", onBackClick = onNavigationBack)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_insert_card),
                    contentDescription = null
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            DisabledInputFieldColor,
                            shape = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp)
                        )
                        .padding(16.dp, 8.dp)
                ) {
                    Text(
                        "Nomor Kartu",
                        color = TextGray,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                    VerticalSpacer(SpacerSize.SMALL)
                    Text(cardNumber)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SecondaryButton(
                modifier = Modifier.weight(1f),
                text = "Batal",
                onClick = onCancel
            )
            PrimaryButton(
                modifier = Modifier.weight(1f),
                text = "Lanjut",
                onClick = onNext
            )
        }
    }
}