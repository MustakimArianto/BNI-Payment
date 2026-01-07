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
        title = "Mini ATM",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToMiniATM
    ),
    MenuItem(
        title = "Sale",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToSale
    ),
    MenuItem(
        title = "Contactless Sale",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToContactlessSale
    ),
    MenuItem(
        title = "Void",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToVoid
    ),
    MenuItem(
        title = "Settlement",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToSettlement
    ),
    MenuItem(
        title = "Installment",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToInstallment
    )
)
