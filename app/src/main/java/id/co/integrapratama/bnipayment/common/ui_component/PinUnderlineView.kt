package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinUnderlineView(
    pinLength: Int = 4,
    value: String,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFFB3C4C5)
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(pinLength) { index ->
            val isFilled = index < value.length

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isFilled) "*" else "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.dp)
                        .background(color = lineColor)
                )
            }
        }
    }
}