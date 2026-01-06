package id.co.integrapratama.bnipayment.feature_sale

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
import id.co.integrapratama.bnipayment.common.ui_component.LoadingDialog
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.saleNavGraph(navController: NavController) {
    navigation<AppRoute.Sale>(
        startDestination = SaleRoute.InputAmount
    ) {

        composable<SaleRoute.InputAmount> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            InputAmountScreen(
                amount = uiState.amount,
                onAmountChanged = { amount -> viewModel.onEvent(SaleUiEvent.OnAmountChange(amount)) },
                onNextClick = { navController.navigateFromCurrent(SaleRoute.InsertCard) },
                onNavigationBack = { navController.navigateToHome() })
        }

        composable<SaleRoute.InsertCard> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.startCardReading()
            }

            LaunchedEffect(uiState.isFinishedReadCard) {
                if (uiState.isFinishedReadCard) {
                    navController.navigateFromCurrent(
                        SaleRoute.ConfirmTransaction, isInclusive = true
                    )
                }
            }


            InsertCardScreen(
                onNavigationBack = { navController.popBackStack() }
            )

            if (uiState.isLoading && uiState.statusMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.statusMessage)
            }
        }

        composable<SaleRoute.ConfirmTransaction> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SaleConfirmTransactionScreen(
                cardNumber = uiState.maskedCardNumber,
                onCancel = { navController.popBackStack() },
                onNext = {
                    navController.navigateFromCurrent(
                        SaleRoute.TransactionStatus,
                        isInclusive = true
                    )
                },
                onNavigationBack = { navController.popBackStack() })
        }

        composable<SaleRoute.TransactionStatus> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SaleTransactionStatus(
                errorMessage = uiState.errorMessage,
                isTransactionSuccess = uiState.isTransactionSuccess,
                onGoToHome = { navController.navigateToHome() },
                onPrintReceipt = { viewModel.printReceipt() }
            )
        }
    }
}