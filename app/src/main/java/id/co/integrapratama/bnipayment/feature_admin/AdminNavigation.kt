package id.co.integrapratama.bnipayment.feature_admin

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.ui_component.ErrorDialog
import id.co.integrapratama.bnipayment.common.ui_component.LoadingDialog
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.adminNavigation(navController: NavController) {
    navigation<AppRoute.AdminSetting>(
        startDestination = AdminRoute.Menu
    ) {
        composable<AdminRoute.Menu> {
            val viewModel = it.sharedViewModel<AdminViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AdminScreen(
                onLogon = {
                    viewModel.performLogon()
                }
            )

            if (uiState.isLoading) {
                LoadingDialog(message = uiState.loadingMessage)
            }

            if (uiState.isError) {
                ErrorDialog(
                    title = "Logon",
                    message = uiState.resultMessage,
                    textButton = "OK",
                    onCloseIconClick = { viewModel.clearUiState() },
                    onPrimaryButtonClicked = { viewModel.clearUiState() })
            }
        }
    }
}