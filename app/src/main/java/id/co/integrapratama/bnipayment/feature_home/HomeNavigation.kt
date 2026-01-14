package id.co.integrapratama.bnipayment.feature_home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import id.co.integrapratama.bnipayment.common.ui_component.SetStatusBarColor
import id.co.integrapratama.bnipayment.feature_installment.installmentNavigation
import id.co.integrapratama.bnipayment.feature_mini_atm.balance_info.miniAtmBalanceInfoNavigation
import id.co.integrapratama.bnipayment.feature_mini_atm.miniATMNavigation
import id.co.integrapratama.bnipayment.feature_sale.saleNavigation
import id.co.integrapratama.bnipayment.feature_void.voidNavigation
import id.co.integrapratama.bnipayment.navigation.AppRoute
import id.co.integrapratama.bnipayment.ui.theme.PrimaryVariantColor

fun NavGraphBuilder.homeNavigation(navController: NavController) {
    navigation<AppRoute.Home>(
        startDestination = HomeRoute.Menu
    ) {
        composable<HomeRoute.Menu> {
            val merchantName = "Cahaya Abadi Lestari"
            val merchantLocation = "Summarecon Mall Bekasi, Bekasi"

            SetStatusBarColor(PrimaryVariantColor)

            HomeMenuScreen(
                merchantName = merchantName,
                merchantLocation = merchantLocation,
                tid = "12345678",
                mid = "123456789012345",
                onNavigateToMiniATM = { navController.navigate(AppRoute.MiniATM) },
                onNavigateToSale = { navController.navigate(AppRoute.Sale(isContactless = false)) },
                onNavigateToContactlessSale = { navController.navigate(AppRoute.Sale(isContactless = true)) },
                onNavigateToVoid = { navController.navigate(AppRoute.Void) },
                onNavigateToSettlement = { navController.navigate(AppRoute.Settlement) },
                onNavigateToInstallment = { navController.navigate(AppRoute.Installment) },
            )
        }
    }

    saleNavigation(navController)
    saleNavigation(navController)
    voidNavigation(navController)
    installmentNavigation(navController)
    miniATMNavigation(navController)
    miniAtmBalanceInfoNavigation(navController)
}
