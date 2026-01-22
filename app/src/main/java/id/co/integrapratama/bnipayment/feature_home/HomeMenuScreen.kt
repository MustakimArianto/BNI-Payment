package id.co.integrapratama.bnipayment.feature_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.MenuItemView
import id.co.integrapratama.bnipayment.common.ui_component.spacer.HorizontalSpacer
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.DividerColor
import id.co.integrapratama.bnipayment.ui.theme.PrimaryTextColor
import id.co.integrapratama.bnipayment.ui.theme.TextGrayColor
import id.co.integrapratama.bnipayment.ui.theme.TextLightBlueColor

@Composable
fun HomeMenuScreen(
    merchantName: String,
    merchantLocation: String,
    tid: String,
    mid: String,
    onNavigateToMiniATM: () -> Unit,
    onNavigateToSale: () -> Unit,
    onNavigateToContactlessSale: () -> Unit,
    onNavigateToVoid: () -> Unit,
    onNavigateToSettlement: () -> Unit,
    onNavigateToInstallment: () -> Unit,
    onNavigateToMerchantCare: () -> Unit,
    onNavigateToTapCash: () -> Unit
) {
    val menuItems = remember {
        getHomeMenuItems(
            onNavigateToMiniATM = onNavigateToMiniATM,
            onNavigateToSale = onNavigateToSale,
            onNavigateToContactlessSale = onNavigateToContactlessSale,
            onNavigateToVoid = onNavigateToVoid,
            onNavigateToSettlement = onNavigateToSettlement,
            onNavigateToInstallment = onNavigateToInstallment,
            onNavigateToTapCash = onNavigateToTapCash
        )
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.ic_top_background),
            contentDescription = null, contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(92.dp),
                    painter = painterResource(R.drawable.ic_splash_logo),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier
                        .size(42.dp)
                        .clickable { onNavigateToMerchantCare() },
                    painter = painterResource(R.drawable.ic_help_outline_white_24),
                    contentDescription = null
                )
            }
            MerchantInformationCard(merchantName, merchantLocation)
            VerticalSpacer(SpacerSize.LARGE)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("TID $tid", color = TextLightBlueColor)
                HorizontalSpacer(SpacerSize.MEDIUM)
                Text(text = "•", fontSize = 32.sp, color = TextLightBlueColor)
                HorizontalSpacer(SpacerSize.MEDIUM)
                Text("MID $mid", color = TextLightBlueColor)
            }
            VerticalSpacer(SpacerSize.MEDIUM)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(menuItems) { item ->
                    MenuItemView(
                        label = item.title, iconRes = item.selectedIcon, onClick = item.onClick
                    )
                }
            }
        }
    }
}

@Composable
fun MerchantInformationCard(
    merchantName: String, merchantLocation: String
) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text("Merchant Name", color = TextGrayColor)
            VerticalSpacer(SpacerSize.MEDIUM)
            Text(merchantName, fontWeight = FontWeight.SemiBold, color = PrimaryTextColor)
            VerticalSpacer(SpacerSize.MEDIUM)
            HorizontalDivider(thickness = 1.dp, color = DividerColor)
            VerticalSpacer(SpacerSize.MEDIUM)
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_black),
                    contentDescription = null
                )
                HorizontalSpacer(SpacerSize.MEDIUM)
                Text(merchantLocation, color = TextLightBlueColor)
            }
        }
    }
}
