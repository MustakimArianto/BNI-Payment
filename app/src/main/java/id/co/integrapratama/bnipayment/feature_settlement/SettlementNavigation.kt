package id.co.integrapratama.bnipayment.feature_settlement

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import id.co.integrapratama.bnipayment.common.ext.navigateFromCurrent
import id.co.integrapratama.bnipayment.common.ext.navigateToHome
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.ui_component.ConfirmationDialog
import id.co.integrapratama.bnipayment.common.ui_component.ErrorDialog
import id.co.integrapratama.bnipayment.common.ui_component.LoadingDialog
import id.co.integrapratama.bnipayment.feature_merchant_pin.merchantPinComposable
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.settlementNavGraph(navController: NavController) {
    navigation<AppRoute.Settlement>(
        startDestination = SettlementRoute.InputMerchantPin
    ) {
        merchantPinComposable<SettlementRoute.InputMerchantPin>(
            navController = navController
        ) {
            val viewModel = it.navBackStackEntry.sharedViewModel<SettlementViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            it.enableMerchantInputPinScope { it2 ->
                val action = it2.action

                action.enableSetTitleScope { it3 ->
                    it3.enableLaunchScope("Settlement")
                }

                action.enableOnAcceptPinScope { it3 ->
                    it3.enableLaunchScope {
                        navController.navigateFromCurrent(
                            SettlementRoute.TotalSettlement,
                            isInclusive = true
                        )
                    }
                }

                action.enableOnCancelPinScope { it3 ->
                    it3.enableLaunchScope {
                        navController.navigateToHome()
                    }
                }
            }
        }

        composable<SettlementRoute.TotalSettlement> {
            val viewModel = it.sharedViewModel<SettlementViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.hasSettlementCompleted) {
                if (uiState.hasSettlementCompleted) {
                    navController.navigateToHome()
                }
            }

            LaunchedEffect("Load Total Settlement") {
                viewModel.onEvent(
                    SettlementUiEvent.LoadTotalSettlement
                )
            }

            if (uiState.totalSettlementSummaryModelResult != null) {
                TotalSettlementSummaryScreen(
                    totalSettlementSummaryModelResult = uiState.totalSettlementSummaryModelResult!!,
                    onBackClick = { navController.navigateToHome() },
                    onStartSettlement = {
                        viewModel.onEvent(
                            SettlementUiEvent.ShowPerformSettlementAndBatchUploadPromptDialog
                        )
                    }
                )
            }

            if (uiState.showPerformSettlementAndBatchUploadPromptDialog) {
                ConfirmationDialog(
                    title = "Settle Transaction",
                    message = "Do you want to settle this transaction?",
                    yesButtonText = "Yes",
                    noButtonText = "No",
                    onYesButtonClicked = {
                        viewModel.onEvent(
                            SettlementUiEvent.ApplyPerformSettlementAndBatchUploadPromptDialog(
                                ApplyPerformSettlementAndBatchUploadPromptDialogType.YES
                            )
                        )
                    },
                    onNoButtonClicked = {
                        viewModel.onEvent(
                            SettlementUiEvent.ApplyPerformSettlementAndBatchUploadPromptDialog(
                                ApplyPerformSettlementAndBatchUploadPromptDialogType.NO
                            )
                        )
                    }
                )
            }

            if (uiState.isLoading && uiState.loadingMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.loadingMessage)
            }

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = "Settlement",
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onButtonClicked = {
                        viewModel.onEvent(
                            SettlementUiEvent.ClearErrorMessage
                        )
                    }
                )
            }
        }
    }
}