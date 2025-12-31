package id.co.integrapratama.bnipayment.feature_mini_atm

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.feature_mini_atm.balance_info.BalanceInfoScreen
import id.co.integrapratama.bnipayment.feature_mini_atm.purchase.PurchaseScreen
import id.co.integrapratama.bnipayment.feature_mini_atm.transfer.TransferScreen
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.miniATMNavGraph(navController: NavController) {
    navigation<AppRoute.MiniATM>(
        startDestination = MiniATMRoute.Menu
    ) {
        composable<MiniATMRoute.Menu> {
            MiniATMMenuScreen(
                onNavigateToInfoSaldo = { navController.navigate(MiniATMRoute.BalanceInfo) },
                onNavigateToTransfer = { navController.navigate(MiniATMRoute.Transfer) },
                onNavigateToPembelian = { navController.navigate(MiniATMRoute.Purchase) },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<MiniATMRoute.BalanceInfo> {
            BalanceInfoScreen(
                onNavigateBack = { navController.navigateUp() }
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