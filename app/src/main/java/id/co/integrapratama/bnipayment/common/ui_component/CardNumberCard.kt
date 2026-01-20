package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.ui.theme.DividerColor
import id.co.integrapratama.bnipayment.ui.theme.TextGrayColor
import id.co.integrapratama.sdk.core.model.AIDName

@Composable
fun CardNumberCard(
    cardNumber: String,
    aidName: AIDName
) {
    Row(
        Modifier
            .background(shape = RoundedCornerShape(16.dp), color = Color.Transparent)
            .fillMaxWidth()
            .border(1.dp, DividerColor, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = stringResource(R.string.message_card_number), color = TextGrayColor)
            Text(text = cardNumber, fontWeight = FontWeight.Bold)
        }

        ImageAidName(aidName)
    }
}