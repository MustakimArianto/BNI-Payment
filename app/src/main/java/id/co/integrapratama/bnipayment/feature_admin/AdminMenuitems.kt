package id.co.integrapratama.bnipayment.feature_admin

import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.model.MenuItem

fun getAdminMenuItems(
    onLogon: () -> Unit
) : List<MenuItem> = listOf(
    MenuItem(
        title = "Logon",
        selectedIcon = R.drawable.ic_government_program,
        onClick = onLogon
    )
)