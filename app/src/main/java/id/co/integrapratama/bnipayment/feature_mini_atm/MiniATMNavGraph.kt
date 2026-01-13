package id.co.integrapratama.bnipayment.feature_mini_atm

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.feature_mini_atm.balance_info.MiniATMBalanceInfoRoute
import id.co.integrapratama.bnipayment.feature_mini_atm.purchase.PurchaseScreen
import id.co.integrapratama.bnipayment.feature_mini_atm.transfer.TransferScreen
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.miniATMNavigation(navController: NavController) {
    navigation<AppRoute.MiniATM>(
        startDestination = MiniATMRoute.Menu
    ) {
        composable<MiniATMRoute.Menu> {
            MiniATMMenuScreen(
                onNavigateToBalanceInfo = { navController.navigate(MiniATMBalanceInfoRoute.Graph) },
                onNavigateToTransfer = { navController.navigate(MiniATMRoute.Transfer) },
                onNavigateToPurchase = { navController.navigate(MiniATMRoute.Purchase) },
            )
        }

        composable<MiniATMRoute.Transfer> {
            TransferScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<MiniATMRoute.Purchase> {
            PurchaseScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}