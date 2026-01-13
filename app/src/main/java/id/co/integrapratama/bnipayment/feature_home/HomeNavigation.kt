package id.co.integrapratama.bnipayment.feature_home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.homeNavigation(navController: NavController) {
    navigation<AppRoute.Home>(
        startDestination = HomeRoute.Menu
    ) {
        composable<HomeRoute.Menu> {
            HomeMenuScreen(
                onNavigateToMiniATM = { navController.navigate(AppRoute.MiniATM) },
                onNavigateToSale = { navController.navigate(AppRoute.Sale(isContactless = false)) },
                onNavigateToContactlessSale = { navController.navigate(AppRoute.Sale(isContactless = true)) },
                onNavigateToVoid = { navController.navigate(AppRoute.Void) },
                onNavigateToSettlement = { navController.navigate(AppRoute.Settlement) },
                onNavigateToInstallment = { navController.navigate(AppRoute.Installment) },
            )
        }
    }
}
