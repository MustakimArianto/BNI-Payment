package id.co.integrapratama.bnipayment.feature_account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.accountNavigation(navController: NavController) {
    navigation<AppRoute.InitMenu>(
        startDestination = InitMenuRoute.Menu
    ) {
        composable<InitMenuRoute.Menu> {
            InitMenuScreen()
        }
    }
}