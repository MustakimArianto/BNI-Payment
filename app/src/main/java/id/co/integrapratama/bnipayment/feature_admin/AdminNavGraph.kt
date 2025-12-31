package id.co.integrapratama.bnipayment.feature_admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.adminNavGraph(navController: NavController) {
    navigation<AppRoute.Admin>(
        startDestination = AdminRoute.Menu
    ) {
        composable<AdminRoute.Menu> {
            AdminScreen()
        }
    }
}