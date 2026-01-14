package id.co.integrapratama.bnipayment.feature_home

import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.model.MenuItem

fun getHomeMenuItems(
    onNavigateToMiniATM: () -> Unit,
    onNavigateToSale: () -> Unit,
    onNavigateToContactlessSale: () -> Unit,
    onNavigateToVoid: () -> Unit,
    onNavigateToSettlement: () -> Unit,
    onNavigateToInstallment: () -> Unit
): List<MenuItem> = listOf(
    MenuItem(
        title = "Sale",
        selectedIcon = R.drawable.ic_sale,
        onClick = onNavigateToSale
    ),
    MenuItem(
        title = "Contactless Sale",
        selectedIcon = R.drawable.ic_contactless,
        onClick = onNavigateToContactlessSale
    ),
    MenuItem(
        title = "Void",
        selectedIcon = R.drawable.ic_void,
        onClick = onNavigateToVoid
    ),
    MenuItem(
        title = "Settlement",
        selectedIcon = R.drawable.ic_settlement,
        onClick = onNavigateToSettlement
    ),
    MenuItem(
        title = "Installment",
        selectedIcon = R.drawable.ic_installment,
        onClick = onNavigateToInstallment
    ),
    MenuItem(
        title = "Mini ATM",
        selectedIcon = R.drawable.ic_sale,
        onClick = onNavigateToMiniATM
    )
)
