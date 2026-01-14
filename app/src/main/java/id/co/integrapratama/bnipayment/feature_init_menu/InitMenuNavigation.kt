package id.co.integrapratama.bnipayment.feature_init_menu

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.common.ext.navigateToHome
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.navigation.AppRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun NavGraphBuilder.initMenuNavigation(navController: NavController) {
    navigation<AppRoute.InitMenu>(
        startDestination = InitMenuRoute.Menu
    ) {
        composable<InitMenuRoute.Menu> {
            val viewModel = it.sharedViewModel<InitMenuViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()

            InitMenuScreen(
                loadingMessage = uiState.loadingMessage,
                errorMessage = uiState.resultMessage,
                currentStep = uiState.currentStep,
                maxStep = uiState.maxStep,
                isLoading = uiState.isLoading,
                isInitSuccess = uiState.isInitSuccess,
                isInitFailed = uiState.isInitFailed,
                onInitSuccess = {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        navController.navigateToHome()
                    }
                },
                onInitFailed = {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        viewModel.clearUiState()
                    }
                },
                onInitializeClick = { viewModel.performLogon() })
        }
    }
}