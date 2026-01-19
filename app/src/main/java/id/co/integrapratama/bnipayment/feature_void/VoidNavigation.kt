package id.co.integrapratama.bnipayment.feature_void

import androidx.activity.compose.BackHandler
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
import id.co.integrapratama.bnipayment.common.ui_component.ErrorDialog
import id.co.integrapratama.bnipayment.common.ui_component.LoadingDialog
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.voidNavigation(navController: NavController) {
    navigation<AppRoute.Void>(
        startDestination = VoidRoute.InputTraceNo
    ) {
        composable<VoidRoute.InputTraceNo> {
            val viewModel = it.sharedViewModel<VoidViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isLoading && uiState.loadingMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.loadingMessage)
            }

            LaunchedEffect(uiState.hasConfirmVoid) {
                if (uiState.hasConfirmVoid) {
                    navController.navigateFromCurrent(VoidRoute.ConfirmVoid, true)
                }
            }

            VoidInputTraceNoScreen(
                title = uiState.title,
                traceNo = uiState.traceNo,
                onTraceNoChanged = { traceNo ->
                    viewModel.onEvent(
                        VoidUiEvent.OnTraceNoChange(traceNo)
                    )
                },
                onNextClick = {
                    if (uiState.traceNo.isNotEmpty()) {
                        viewModel.onEvent(
                            VoidUiEvent.ConfirmVoid
                        )
                    } else {
                        viewModel.onEvent(
                            VoidUiEvent.SetErrorMessage(
                                "Nominal tidak boleh kosong"
                            )
                        )
                    }
                },
                onNavigationBack = { navController.navigateToHome() }
            )

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onButtonClicked = { viewModel.clearErrorMessage() }
                )
            }
        }

        composable<VoidRoute.ConfirmVoid> {
            val viewModel = it.sharedViewModel<VoidViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isTransactionFinished) {
                    navController.navigateFromCurrent(VoidRoute.TransactionStatus, true)
                }
            }

            if (uiState.isLoading && uiState.loadingMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.loadingMessage)
            }

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onButtonClicked = { navController.navigateToHome() })
            }
            if (uiState.voidRequestModel != null) {
                VoidConfirmTransactionScreen(
                    voidRequestModel = uiState.voidRequestModel!!,
                    onSubmitVoid = {
                        viewModel.onEvent(
                            VoidUiEvent.SubmitVoid
                        )
                    },
                    onBackClick = {
                        navController.navigateToHome()
                    }
                )
            }
        }

        composable<VoidRoute.TransactionStatus> {
            fun navigateToHome() {
                navController.navigateToHome()
            }

            BackHandler {
                navigateToHome()
            }

            val viewModel = it.sharedViewModel<VoidViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isLoading && uiState.loadingMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.loadingMessage)
            }

            VoidTransactionStatus(
                transactionResultMessage = uiState.transactionResultMessage,
                onGoToHome = { navigateToHome() },
                onPrintReceipt = {
                    viewModel.onEvent(
                    VoidUiEvent.PrintReceipt
                    )
                }
            )
        }
    }
}