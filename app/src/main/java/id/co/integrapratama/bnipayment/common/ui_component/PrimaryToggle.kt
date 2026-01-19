package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.ui.theme.PrimaryVariantColor
import id.co.integrapratama.bnipayment.ui.theme.ToggleDisabledColor

@Composable
fun PrimaryToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val backgroundColor = if (checked) PrimaryVariantColor else ToggleDisabledColor
    val thumbOffset = if (checked) 16.dp else 0.dp

    Surface(
        modifier = modifier
            .width(42.dp)
            .height(22.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        onClick = { if (enabled) onCheckedChange(!checked) },
        enabled = enabled
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset + 4.dp)
                    .size(14.dp)
                    .background(Color.White, CircleShape)
            )
        }
    }
}