package id.co.integrapratama.bnipayment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import id.co.integrapratama.bnipayment.feature_account.accountNavGraph
import id.co.integrapratama.bnipayment.feature_admin.adminNavGraph
import id.co.integrapratama.bnipayment.feature_home.homeNavGraph
import id.co.integrapratama.bnipayment.feature_information.informationNavGraph
import id.co.integrapratama.bnipayment.feature_installment.installmentNavGraph
import id.co.integrapratama.bnipayment.feature_menu.mainNavGraph
import id.co.integrapratama.bnipayment.feature_mini_atm.balance_info.miniAtmBalanceInfoNavigation
import id.co.integrapratama.bnipayment.feature_mini_atm.miniATMNavGraph
import id.co.integrapratama.bnipayment.feature_onboarding.onboardingNavigation
import id.co.integrapratama.bnipayment.feature_sale.saleNavigation
import id.co.integrapratama.bnipayment.feature_void.voidNavGraph

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Onboarding
    ) {
        onboardingNavigation(navController)
        mainNavGraph(navController)
        homeNavGraph(navController)
        informationNavGraph(navController)
        adminNavGraph(navController)
        accountNavGraph(navController)
        saleNavigation(navController)
        voidNavGraph(navController)
        installmentNavGraph(navController)

        // Mini ATM
        miniATMNavGraph(navController)
        miniAtmBalanceInfoNavigation(navController)
    }
}