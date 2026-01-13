package id.co.integrapratama.bnipayment.feature_home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.common.ui_component.MenuItemView

@Composable
fun HomeMenuScreen(
    onNavigateToMiniATM: () -> Unit,
    onNavigateToSale: () -> Unit,
    onNavigateToContactlessSale: () -> Unit,
    onNavigateToVoid: () -> Unit,
    onNavigateToSettlement: () -> Unit,
    onNavigateToInstallment: () -> Unit
) {
    val menuItems = remember {
        getHomeMenuItems(
            onNavigateToMiniATM = onNavigateToMiniATM,
            onNavigateToSale = onNavigateToSale,
            onNavigateToContactlessSale = onNavigateToContactlessSale,
            onNavigateToVoid = onNavigateToVoid,
            onNavigateToSettlement = onNavigateToSettlement,
            onNavigateToInstallment = onNavigateToInstallment
        )
    }

    Column(Modifier
        .padding(16.dp)
        .navigationBarsPadding()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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

