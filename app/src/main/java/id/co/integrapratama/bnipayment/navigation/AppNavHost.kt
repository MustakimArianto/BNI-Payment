package id.co.integrapratama.bnipayment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import id.co.integrapratama.bnipayment.feature_account.accountNavigation
import id.co.integrapratama.bnipayment.feature_admin.adminNavigation
import id.co.integrapratama.bnipayment.feature_home.homeNavigation
import id.co.integrapratama.bnipayment.feature_installment.installmentNavigation
import id.co.integrapratama.bnipayment.feature_menu.menuNavigation
import id.co.integrapratama.bnipayment.feature_mini_atm.balance_info.miniAtmBalanceInfoNavigation
import id.co.integrapratama.bnipayment.feature_mini_atm.miniATMNavigation
import id.co.integrapratama.bnipayment.feature_onboarding.onboardingNavigation
import id.co.integrapratama.bnipayment.feature_sale.saleNavigation
import id.co.integrapratama.bnipayment.feature_void.voidNavigation

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Onboarding
    ) {
        onboardingNavigation(navController)
        menuNavigation(navController)
        homeNavigation(navController)
        adminNavigation(navController)
        accountNavigation(navController)
        saleNavigation(navController)
        voidNavigation(navController)
        installmentNavigation(navController)

        // Mini ATM
        miniATMNavigation(navController)
        miniAtmBalanceInfoNavigation(navController)
    }
}