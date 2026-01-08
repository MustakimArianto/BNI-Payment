package id.co.integrapratama.bnipayment.feature_home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.navigation.AppRoute

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

    Column(Modifier.padding(16.dp).navigationBarsPadding()) {
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
                modifier = Modifier.size(82.dp),
                painter = painterResource(iconRes),
                contentDescription = label
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}