package id.co.integrapratama.bnipayment.feature_menu

import androidx.annotation.DrawableRes
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.navigation.AppRoute

enum class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val route: AppRoute
) {
    HOME(
        title = "Beranda",
        icon = R.drawable.ic_home_filled_white_24,
        route = AppRoute.Home
    ),
    INFORMATION(
        title = "Informasi",
        icon = R.drawable.ic_notifications_filled_white_24,
        route = AppRoute.Information
    ),
    REPORT(
        title = "Admin",
        icon = R.drawable.ic_report_filled_white_24,
        route = AppRoute.Admin
    ),
    ACCOUNT(
        title = "Akun",
        icon = R.drawable.ic_account_circle_filled_white_24,
        route = AppRoute.Account
    )
}