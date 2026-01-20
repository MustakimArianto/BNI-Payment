package id.co.integrapratama.bnipayment.common.ui_component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.TextLightBlueColor

@Composable
fun MenuItemView(
    label: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(62.dp),
                painter = painterResource(iconRes),
                contentDescription = label
            )
        }

        VerticalSpacer(SpacerSize.SMALL)
        Text(
            text = label,
            fontSize = 12.sp,
            maxLines = 2,
            textAlign = TextAlign.Center,
            color = TextLightBlueColor
        )
    }
}