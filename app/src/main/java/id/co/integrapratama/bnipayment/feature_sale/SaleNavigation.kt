package id.co.integrapratama.bnipayment.feature_sale

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
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
import id.co.integrapratama.bnipayment.common.ui_component.SetStatusBarColor
import id.co.integrapratama.bnipayment.navigation.AppRoute
import id.co.integrapratama.bnipayment.ui.theme.PrimaryColor

fun NavGraphBuilder.saleNavigation(navController: NavController) {
    navigation<AppRoute.Sale>(
        startDestination = SaleRoute.InputAmount
    ) {
        composable<SaleRoute.InputAmount> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SetStatusBarColor(PrimaryColor)

            BackHandler {
                navController.navigateToHome()
            }

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
                tip = uiState.tip,
                onAmountChanged = { amount -> viewModel.onEvent(SaleUiEvent.OnAmountChange(amount)) },
                onTipChanged = { tip ->
                    viewModel.onEvent(SaleUiEvent.OnTipChange(tip))
                },
                onOkClick = {
                    if (uiState.amount.isNotEmpty() || uiState.amount == "0") {
                        navController.navigateFromCurrent(SaleRoute.InsertCard)
                    } else {
                        viewModel.setErrorMessage("Amount cannot be empty")
                    }
                },
            )

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Okay",
                    onButtonClicked = { viewModel.clearErrorMessage() })
            }
        }

        composable<SaleRoute.InsertCard> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SetStatusBarColor(Color.White, darkIcons = true)

            BackHandler {
                navController.navigateToHome()
            }

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
                isReadingCard = uiState.isReadingCard
            )

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isTransactionFinished) {
                    navController.navigateFromCurrent(SaleRoute.TransactionStatus, true)
                }
            }

            if (uiState.errorMessage.isNotEmpty()) {

                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Ok", onButtonClicked = { navController.navigateToHome() })
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
                    textButton = "Ok", onButtonClicked = { navController.navigateToHome() })
            }

            SaleConfirmTransactionScreen(
                title = uiState.title,
                pin = uiState.pin,
                cardNumber = uiState.cardNumber,
                isPhysicalKeyboard = uiState.isPhysicalKeyboard,
                showPinpad = uiState.isShowPinpad,
                showOfflinePinpad = uiState.isShowOfflinePinpad,
                onNext = { viewModel.onEvent(SaleUiEvent.OnConfirmCard) },
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