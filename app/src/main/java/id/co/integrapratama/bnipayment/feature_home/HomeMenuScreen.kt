package id.co.integrapratama.bnipayment.feature_home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal fun HomeMenuScreen(
    onNavigateToMiniATM: () -> Unit,
    onNavigateToSale: () -> Unit,
    onNavigateToContactlessSale: () -> Unit,
    onNavigateToVoid: () -> Unit,
    onNavigateToSettlement: () -> Unit
) {
    Text(text = "Home Menu Screen")
}