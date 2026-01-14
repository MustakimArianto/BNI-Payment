package id.co.integrapratama.bnipayment.common.ext

import androidx.navigation.NavController
import id.co.integrapratama.bnipayment.navigation.AppRoute

inline fun <reified T : Any> NavController.navigateFromCurrent(
    route: T,
    isInclusive: Boolean = false,
) {
    navigate(route) {
        popUpTo(currentBackStackEntry!!.destination.id) {
            inclusive = isInclusive
        }

        launchSingleTop = true
    }
}

fun NavController.navigateToHome() {
    navigate(AppRoute.Home) {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
