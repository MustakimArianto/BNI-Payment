package id.co.integrapratama.bnipayment.feature_admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.common.ui_component.MenuItemView
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.DividerColor
import id.co.integrapratama.bnipayment.ui.theme.TextGrayColor

@Composable
fun AdminScreen(
    title: String,
    appVersion: String,
    onMerchantInfo: () -> Unit,
    onTestConnection: () -> Unit,
    onTerminalInfo: () -> Unit,
    onReaderLogon: () -> Unit,
    onEcr: () -> Unit,
    onBatchInfo: () -> Unit,
    onSetBatch: () -> Unit,
    onSettingConnection: () -> Unit,
    onEcrH2H: () -> Unit,
    onHostSetup: () -> Unit,
    onPOSExtPort: () -> Unit,
    onDeleteReversal: () -> Unit,
    onLanguage: () -> Unit,
) {
    val menuItems = remember {
        getAdminMenuItems(
            onMerchantInfo = onMerchantInfo,
            onTestConnection = onTestConnection,
            onTerminalInfo = onTerminalInfo,
            onReaderLogon = onReaderLogon,
            onEcr = onEcr,
            onBatchInfo = onBatchInfo,
            onSetBatch = onSetBatch,
            onSettingConnection = onSettingConnection,
            onEcrH2H = onEcrH2H,
            onHostSetup = onHostSetup,
            onPOSExtPort = onPOSExtPort,
            onDeleteReversal = onDeleteReversal,
            onLanguage = onLanguage
        )
    }

    Column(
        Modifier
            .systemBarsPadding()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        VerticalSpacer(SpacerSize.LARGE)
        HorizontalDivider(thickness = 3.dp, color = DividerColor)
        VerticalSpacer(SpacerSize.X_LARGE)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Ver. $appVersion",
            fontSize = 14.sp,
            color = TextGrayColor,
            textAlign = TextAlign.Center
        )
        VerticalSpacer(SpacerSize.X_LARGE)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuItems) { item ->
                MenuItemView(
                    label = item.title,
                    iconRes = item.selectedIcon,
                    onClick = item.onClick
                )
            }
        }
    }
}