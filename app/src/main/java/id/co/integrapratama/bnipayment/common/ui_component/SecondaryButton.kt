package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.ui.theme.TextGrayColor

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isBold: Boolean = false,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(8.dp),
    isEnabled: Boolean = true
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(),
        shape = roundedCornerShape,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = TextGrayColor,
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        onClick = onClick
    ) {
        val fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal

        Text(text = text, fontWeight = fontWeight)
    }
}