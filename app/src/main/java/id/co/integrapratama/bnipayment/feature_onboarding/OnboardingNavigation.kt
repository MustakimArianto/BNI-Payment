package id.co.integrapratama.bnipayment.feature_onboarding

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.common.ext.navigateFromCurrent
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.ui_component.SetStatusBarColor
import id.co.integrapratama.bnipayment.navigation.AppRoute
import kotlinx.coroutines.delay

fun NavGraphBuilder.onboardingNavigation(navController: NavController) {
    navigation<AppRoute.Onboarding>(
        startDestination = OnboardingRoute.Splash
    ) {

        composable<OnboardingRoute.Splash> {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navController)

            LaunchedEffect(Unit) {
                delay(1500L)
                if (viewModel.isFirstInstall()) {
                    navController.navigateFromCurrent(OnboardingRoute.Welcome, true)
                } else {
                    navController.navigateFromCurrent(AppRoute.Main, true)
                }
            }
            SplashScreen(viewModel.getAppVersion())
        }

        composable<OnboardingRoute.Welcome> {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navController)

            SetStatusBarColor(Color.White, true)

            WelcomeScreen(
                onEnterClick = {
                    viewModel.updateFirstInstall()
                    navController.navigateFromCurrent(AppRoute.Main, true)
                }
            )
        }
    }
}