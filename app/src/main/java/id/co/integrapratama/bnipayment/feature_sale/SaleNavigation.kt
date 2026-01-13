package id.co.integrapratama.bnipayment.feature_sale

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import id.co.integrapratama.bnipayment.common.ext.navigateFromCurrent
import id.co.integrapratama.bnipayment.common.ext.navigateToHome
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.ui_component.ErrorDialog
import id.co.integrapratama.bnipayment.common.ui_component.LoadingDialog
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.saleNavigation(navController: NavController) {
    navigation<AppRoute.Sale>(
        startDestination = SaleRoute.InputAmount
    ) {
        composable<SaleRoute.InputAmount> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.clearUiState()

                val parentSaleRoute = navController
                    .getBackStackEntry<AppRoute.Sale>()
                    .toRoute<AppRoute.Sale>()

                val isContactless = parentSaleRoute.isContactless
                viewModel.onEvent(SaleUiEvent.SetContactless(isContactless))
            }

            InputAmountScreen(
                title = uiState.title,
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
                        SaleRoute.ConfirmCard,
                        isInclusive = true
                    )
                }
            }

            InsertCardScreen(
                title = uiState.title,
                onNavigationBack = { navController.popBackStack() }
            )

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isTransactionFinished) {
                    navController.navigateFromCurrent(SaleRoute.TransactionStatus, true)
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
                    onCloseIconClick = {
                        navController.navigateToHome()
                    },
                    onPrimaryButtonClicked = { navController.navigateToHome() })
            }
        }

        composable<SaleRoute.ConfirmCard> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isShowPinpad) {
                BackHandler {

                }
            }

            if (uiState.isShowOfflinePinpad) {
                BackHandler {

                }
            }

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isTransactionFinished) {
                    navController.navigateFromCurrent(SaleRoute.TransactionStatus, true)
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
                    onCloseIconClick = {
                        navController.navigateToHome()
                    },
                    onPrimaryButtonClicked = { navController.navigateToHome() })
            }
            SaleConfirmTransactionScreen(
                title = uiState.title,
                pin = uiState.pin,
                isPhysicalKeyboard = uiState.isPhysicalKeyboard,
                cardNumber = uiState.maskedCardNumber,
                onCancel = { navController.popBackStack() },
                onNext = {
                    viewModel.onEvent(SaleUiEvent.OnConfirmCard)
                },
                showPinpad = uiState.isShowPinpad,
                showOfflinePinpad = uiState.isShowOfflinePinpad,
                onButtonMapReady = { containerInfo, pinpadMap ->
                    viewModel.onEvent(
                        SaleUiEvent.MappingPinpad(
                            containerInfo = containerInfo,
                            pinpadMap = pinpadMap
                        )
                    )
                },
                onOfflinePinButtonMapReady = { containerInfo, pinpadMap ->
                    viewModel.onEvent(
                        SaleUiEvent.MappingOfflinePinpad(
                            containerInfo = containerInfo,
                            pinpadMap = pinpadMap
                        )
                    )
                },
                onNavigationBack = { navController.popBackStack() }
            )
        }

        composable<SaleRoute.TransactionStatus> {
            fun navigateToHome() {
                navController.navigateToHome()
            }

            BackHandler {
                navigateToHome()
            }

            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isLoading && uiState.loadingMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.loadingMessage)
            }

            SaleTransactionStatus(
                transactionResultMessage = uiState.transactionResultMessage,
                onGoToHome = { navigateToHome() },
                onPrintReceipt = { viewModel.printReceipt() }
            )
        }
    }
}