package id.co.integrapratama.bnipayment.feature_account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.accountNavGraph(navController: NavController) {
    navigation<AppRoute.Account>(
        startDestination = AccountRoute.Menu
    ) {
        composable<AccountRoute.Menu> {
            AccountScreen()
        }
    }
}