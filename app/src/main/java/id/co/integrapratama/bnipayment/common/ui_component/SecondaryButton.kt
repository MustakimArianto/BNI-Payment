package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.ui.theme.SecondaryColor

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isBold: Boolean = false,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(22.dp),
    isEnabled: Boolean = true
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(18.dp),
        shape = roundedCornerShape,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = SecondaryColor,
            contentColor = Color.White,
        ),
        onClick = onClick
    ) {
        val fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal

        Text(text = text, fontWeight = fontWeight)
    }
}