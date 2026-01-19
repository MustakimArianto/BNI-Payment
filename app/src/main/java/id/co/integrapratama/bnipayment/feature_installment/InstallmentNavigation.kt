package id.co.integrapratama.bnipayment.feature_installment

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

fun NavGraphBuilder.installmentNavigation(navController: NavController) {
    navigation<AppRoute.Installment>(
        startDestination = InstallmentRoute.InputAmount
    ) {

        composable<InstallmentRoute.InputAmount> {
            val viewModel = it.sharedViewModel<InstallmentViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.clearUiState()
                viewModel.getInstallmentPeriodAndPlanList()
            }

            InstallmentInputAmountScreen(
                amount = uiState.amount,
                onAmountChanged = { amount ->
                    viewModel.onEvent(
                        InstallmentUiEvent.OnAmountChange(
                            amount
                        )
                    )
                },
                onNextClick = {
                    if (uiState.amount.isNotEmpty()) {
                        navController.navigateFromCurrent(InstallmentRoute.InsertCard)
                    } else {
                        viewModel.setErrorMessage("Nominal tidak boleh kosong")
                    }
                },
                onNavigationBack = { navController.navigateToHome() },
                installmentPlanListResource = uiState.installmentPlanListResource,
                selectedInstallmentPlan = uiState.selectedInstallmentPlan,
                onSelectInstallmentPlan = { installmentPlan ->
                    viewModel.onEvent(
                        InstallmentUiEvent.OnInstallmentPlanSelectionChange(
                            installmentPlan
                        )
                    )
                },
                installmentPeriodListResource = uiState.installmentPeriodListResource,
                selectedInstallmentPeriod = uiState.selectedInstallmentPeriod,
                onSelectInstallmentPeriod = { installmentPeriod ->
                    viewModel.onEvent(
                        InstallmentUiEvent.OnInstallmentPeriodSelectionChange(
                            installmentPeriod
                        )
                    )
                },
            )

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onButtonClicked = { viewModel.clearErrorMessage() })
            }
        }

        composable<InstallmentRoute.InsertCard> {
            val viewModel = it.sharedViewModel<InstallmentViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.startCardReading()
            }

            LaunchedEffect(uiState.isFinishedReadCard) {
                if (uiState.isFinishedReadCard) {
                    navController.navigateFromCurrent(
                        InstallmentRoute.ConfirmTransaction,
                        isInclusive = true
                    )
                }
            }

            InstallmentInsertCardScreen(
                onNavigationBack = { navController.popBackStack() }
            )

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

        composable<InstallmentRoute.ConfirmTransaction> {
            val viewModel = it.sharedViewModel<InstallmentViewModel>(navController)
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
                if (uiState.isCardConfirmed) {
                    navController.navigateFromCurrent(InstallmentRoute.TransactionStatus, true)
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
            InstallmentConfirmTransactionScreen(
                pin = uiState.pin,
                isPhysicalKeyboard = uiState.isPhysicalKeyboard,
                cardNumber = uiState.maskedCardNumber,
                onCancel = { navController.popBackStack() },
                onNext = {
                    viewModel.onEvent(InstallmentUiEvent.OnConfirmCard)
                },
                showPinpad = uiState.isShowPinpad,
                showOfflinePinpad = uiState.isShowOfflinePinpad,
                onButtonMapReady = { containerInfo, pinpadMap ->
                    viewModel.onEvent(
                        InstallmentUiEvent.MappingPinpad(
                            containerInfo = containerInfo,
                            pinpadMap = pinpadMap
                        )
                    )
                },
                onOfflinePinButtonMapReady = { containerInfo, pinpadMap ->
                    viewModel.onEvent(
                        InstallmentUiEvent.MappingOfflinePinpad(
                            containerInfo = containerInfo,
                            pinpadMap = pinpadMap
                        )
                    )
                },
                onNavigationBack = { navController.popBackStack() }
            )
        }

        composable<InstallmentRoute.TransactionStatus> {
            val viewModel = it.sharedViewModel<InstallmentViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            InstallmentTransactionStatus(
                loadingMessage = uiState.loadingMessage,
                isLoading = uiState.isLoading,
                transactionResultMessage = uiState.transactionResultMessage,
                onGoToHome = { navController.navigateToHome() },
                onPrintReceipt = { viewModel.printReceipt() }
            )
        }
    }
}