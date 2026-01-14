package id.co.integrapratama.bnipayment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import id.co.integrapratama.bnipayment.feature_admin.adminNavigation
import id.co.integrapratama.bnipayment.feature_home.homeNavigation
import id.co.integrapratama.bnipayment.feature_init_menu.initMenuNavigation
import id.co.integrapratama.bnipayment.feature_menu.menuNavigation
import id.co.integrapratama.bnipayment.feature_onboarding.onboardingNavigation

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Onboarding
    ) {
        onboardingNavigation(navController)
        menuNavigation(navController)
    }
}