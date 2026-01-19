package id.co.integrapratama.bnipayment.feature_mini_atm.balance_info

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.common.ext.navigateFromCurrent
import id.co.integrapratama.bnipayment.common.ext.navigateToHome
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.ui_component.ErrorDialog
import id.co.integrapratama.bnipayment.common.ui_component.LoadingDialog

fun NavGraphBuilder.miniAtmBalanceInfoNavigation(navController: NavController) {
    navigation<MiniATMBalanceInfoRoute.Graph>(
        startDestination = MiniATMBalanceInfoRoute.InsertCard
    ) {
        composable<MiniATMBalanceInfoRoute.InsertCard> {
            val viewModel = it.sharedViewModel<MiniATMBalanceInfoViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.startCardReading()
            }

            LaunchedEffect(uiState.isFinishedReadCard) {
                if (uiState.isFinishedReadCard) {
                    navController.navigateFromCurrent(
                        MiniATMBalanceInfoRoute.ConfirmCard, isInclusive = true
                    )
                }
            }

            LaunchedEffect(uiState.isFinishedReadCard) {
                if (uiState.isFinishedReadCard) {
                    navController.navigateFromCurrent(
                        MiniATMBalanceInfoRoute.ConfirmCard, isInclusive = true
                    )
                }
            }

            MiniATMBalanceInfoInsertCardScreen(
                title = uiState.title, onNavigationBack = { navController.navigateUp() })

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isTransactionFinished) {
                    navController.navigateFromCurrent(
                        MiniATMBalanceInfoRoute.TransactionStatus, true
                    )
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
        }

        composable<MiniATMBalanceInfoRoute.ConfirmCard> {
            val viewModel = it.sharedViewModel<MiniATMBalanceInfoViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isShowPinpad) {
                BackHandler { }
            }

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isTransactionFinished) {
                    navController.navigateFromCurrent(
                        MiniATMBalanceInfoRoute.TransactionStatus, true
                    )
                }
            }

            LaunchedEffect(uiState.cardReadOutput) {
                if (uiState.cardReadOutput?.insertMode?.equals("INSERT", true) == true) {
                    viewModel.onEvent(MiniATMBalanceInfoUiEvent.SetContactless(false))
                }
            }

            MiniATMBalanceInfoConfirmCardScreen(
                title = uiState.title,
                pin = uiState.pin,
                isPhysicalKeyboard = uiState.isPhysicalKeyboard,
                cardNumber = uiState.maskedCardNumber,
                onCancel = { navController.popBackStack() },
                onNext = {
                    viewModel.onEvent(MiniATMBalanceInfoUiEvent.OnConfirmCard)
                },
                showPinpad = uiState.isShowPinpad,
                showOfflinePinpad = uiState.isShowOfflinePinpad,
                onButtonMapReady = { containerInfo, pinpadMap ->
                    viewModel.onEvent(
                        MiniATMBalanceInfoUiEvent.MappingPinpad(
                            containerInfo = containerInfo, pinpadMap = pinpadMap
                        )
                    )
                },
                onOfflinePinButtonMapReady = { containerInfo, pinpadMap ->
                    viewModel.onEvent(
                        MiniATMBalanceInfoUiEvent.MappingOfflinePinpad(
                            containerInfo = containerInfo, pinpadMap = pinpadMap
                        )
                    )
                },
                onNavigationBack = { navController.popBackStack() })
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

        }

        composable<MiniATMBalanceInfoRoute.TransactionStatus> {
            val viewModel = it.sharedViewModel<MiniATMBalanceInfoViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isLoading && uiState.loadingMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.loadingMessage)
            }
            MiniATMBalanceInfoTransactionStatus(
                transactionResultMessage = "",
                onGoToHome = { navController.navigateToHome() },
                onPrintReceipt = {
                    viewModel.printReceipt()
                })
        }
    }
}