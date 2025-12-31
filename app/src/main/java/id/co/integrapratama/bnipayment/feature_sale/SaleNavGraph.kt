package id.co.integrapratama.bnipayment.feature_sale

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.saleNavGraph(navController: NavController) {
    navigation<AppRoute.Sale>(
        startDestination = SaleRoute.InputAmount
    ) {
        composable<SaleRoute.InputAmount> {
            InputAmountScreen()
        }
    }
}