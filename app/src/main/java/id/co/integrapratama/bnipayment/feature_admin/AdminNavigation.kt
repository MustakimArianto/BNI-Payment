package id.co.integrapratama.bnipayment.feature_admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.adminNavigation(navController: NavController) {
    navigation<AppRoute.AdminSetting>(
        startDestination = AdminRoute.Menu
    ) {
        composable<AdminRoute.Menu> {
            val viewModel = it.sharedViewModel<AdminViewModel>(navController)

            AdminScreen()
        }
    }
}