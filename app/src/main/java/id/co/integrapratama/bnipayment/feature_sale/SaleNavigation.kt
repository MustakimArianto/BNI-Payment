package id.co.integrapratama.bnipayment.feature_sale

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

fun NavGraphBuilder.saleNavGraph(navController: NavController) {
    navigation<AppRoute.Sale>(
        startDestination = SaleRoute.InputAmount
    ) {

        composable<SaleRoute.InputAmount> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.clearUiState()
            }

            InputAmountScreen(
                amount = uiState.amount,
                onAmountChanged = { amount -> viewModel.onEvent(SaleUiEvent.OnAmountChange(amount)) },
                onNextClick = {
                    if (uiState.amount.isNotEmpty()) {
                        navController.navigateFromCurrent(SaleRoute.InsertCard)
                    } else {
                        viewModel.setErrorMessage("Nominal tidak boleh kosong")
                    }
                },
                onNavigationBack = { navController.navigateToHome() }
            )

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onCloseIconClick = {
                        viewModel.clearErrorMessage()
                    },
                    onPrimaryButtonClicked = { viewModel.clearErrorMessage() })
            }
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
                        SaleRoute.ConfirmTransaction,
                        isInclusive = true
                    )
                }
            }

            InsertCardScreen(
                onNavigationBack = { navController.popBackStack() }
            )

            if (uiState.isLoading && uiState.statusMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.statusMessage)
            }

            if (uiState.errorMessage.isNotEmpty()) {

                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onCloseIconClick = {
                        navController.navigateToHome()
                    },
                    onPrimaryButtonClicked = { navController.navigateToHome() })
            }
        }

        composable<SaleRoute.ConfirmTransaction> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isShowPinpad) {
                BackHandler {

                }
            }

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isCardConfirmed) {
                    navController.navigateFromCurrent(SaleRoute.TransactionStatus, true)
                }
            }

            if (uiState.isLoading && uiState.statusMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.statusMessage)
            }

            if (uiState.errorMessage.isNotEmpty()) {

                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onCloseIconClick = {
                        navController.navigateToHome()
                    },
                    onPrimaryButtonClicked = { navController.navigateToHome() })
            }
            SaleConfirmTransactionScreen(
                pin = uiState.pin,
                isPhysicalKeyboard = uiState.isPhysicalKeyboard,
                cardNumber = uiState.maskedCardNumber,
                onCancel = { navController.popBackStack() },
                onNext = {
                    viewModel.onEvent(SaleUiEvent.OnConfirmCard)
                },
                showPinpad = uiState.isShowPinpad,
                onButtonMapReady = { containerInfo, pinpadMap ->
                    viewModel.onEvent(
                        SaleUiEvent.MappingPinpad(
                            containerInfo = containerInfo,
                            pinpadMap = pinpadMap
                        )
                    )
                },
                onNavigationBack = { navController.popBackStack() })
        }

        composable<SaleRoute.TransactionStatus> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SaleTransactionStatus(
                errorMessage = uiState.errorMessage,
                isTransactionFinished = uiState.isTransactionFinished,
                onGoToHome = { navController.navigateToHome() },
                onPrintReceipt = { viewModel.printReceipt() }
            )
        }
    }
}