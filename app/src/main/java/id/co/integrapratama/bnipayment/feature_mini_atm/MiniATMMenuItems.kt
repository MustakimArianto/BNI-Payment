package id.co.integrapratama.bnipayment.feature_mini_atm

import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.model.MenuItem

fun getMiniATMMenuItems(
    onNavigateToBalanceInfo: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToPurchase: () -> Unit,
): List<MenuItem> = listOf(
    MenuItem(
        title = "Info Saldo",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToBalanceInfo
    ),
    MenuItem(
        title = "Transfer",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToTransfer
    ),
    MenuItem(
        title = "Pembelian",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onNavigateToPurchase
    )
)