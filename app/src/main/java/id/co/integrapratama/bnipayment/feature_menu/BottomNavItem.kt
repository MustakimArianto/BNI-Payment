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
        title = "Home", icon = R.drawable.ic_home_bottombar, route = AppRoute.Home
    ),
    ADMIN_SETTING(
        title = "Admin Setting", icon = R.drawable.ic_admin_bottombar, route = AppRoute.AdminSetting
    ),
    INIT_MENU(
        title = "Initialized Menu", icon = R.drawable.ic_init_bottombar, route = AppRoute.InitMenu
    )
}