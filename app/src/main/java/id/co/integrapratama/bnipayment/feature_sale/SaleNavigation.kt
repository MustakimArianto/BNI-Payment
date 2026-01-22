package id.co.integrapratama.bnipayment.feature_sale

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ext.navigateFromCurrent
import id.co.integrapratama.bnipayment.common.ext.navigateToHome
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.ui_component.ErrorDialog
import id.co.integrapratama.bnipayment.common.ui_component.SetStatusBarColor
import id.co.integrapratama.bnipayment.common.ui_component.TransactionScreenReceipt
import id.co.integrapratama.bnipayment.navigation.AppRoute
import id.co.integrapratama.bnipayment.ui.theme.PrimaryColor
import id.co.integrapratama.sdk.core.utils.StringUtil

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
                pin = uiState.pin,
                isPhysicalKeyboard = uiState.isPhysicalKeyboard,
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
                onOkClick = {
                    if (uiState.amount.isNotEmpty() || uiState.amount == "0") {
                        if (!uiState.isContactless) {
                            navController.navigateFromCurrent(
                                SaleRoute.InsertCard, isInclusive = true
                            )
                        }
                        viewModel.onEvent(SaleUiEvent.OnConfirmContactless)
                    } else {
                        viewModel.setErrorMessage("Amount cannot be empty")
                    }
                },
            )

            if (uiState.isShowContactlessDialog) {
                SaleContactlessDialog(
                    uiState.title,
                    stringResource(R.string.message_tap_card)
                )
            }

            LaunchedEffect(uiState.isProcessing) {
                if (uiState.isProcessing) {
                    navController.navigateFromCurrent(
                        SaleRoute.ProcessingTransaction, isInclusive = true
                    )
                }
            }

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
                    textButton = "Ok",
                    onButtonClicked = { navController.navigateToHome() })
            }
        }

        composable<SaleRoute.ConfirmCard> {
            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SetStatusBarColor(PrimaryColor)

            if (uiState.isShowPinpad || uiState.isShowOfflinePinpad) {
                BackHandler {
                    // Prevent back navigation during PIN entry
                }
            } else {
                BackHandler {
                    navController.navigateToHome()
                }
            }

            LaunchedEffect(uiState.isProcessing) {
                if (uiState.isProcessing) {
                    navController.navigateFromCurrent(
                        SaleRoute.ProcessingTransaction, isInclusive = true
                    )
                }
            }

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onButtonClicked = { navController.navigateToHome() })
            }

            SaleConfirmTransactionScreen(
                title = uiState.title,
                amount = uiState.amount,
                tip = uiState.tip,
                total = (uiState.amount.toLong() + uiState.tip.ifEmpty { "0" }.toLong()).toString(),
                pin = uiState.pin,
                aidName = uiState.aidName,
                cardNumber = uiState.maskedCardNumber,
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

        composable<SaleRoute.ProcessingTransaction> {
            SetStatusBarColor(Color.Transparent, darkIcons = true)

            BackHandler {

            }

            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isTransactionFinished) {
                    viewModel.printReceipt()
                }
            }

            LaunchedEffect(uiState.isPrintingFinished) {
                if (uiState.isPrintingFinished) {
                    navController.navigateFromCurrent(SaleRoute.TransactionStatus, true)
                }
            }

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = uiState.title,
                    message = uiState.errorMessage,
                    textButton = stringResource(R.string.text_okay),
                    onButtonClicked = { viewModel.clearErrorMessage() })
            }

            SaleProcessingScreen(
                isProcessing = uiState.isProcessing,
            )

        }

        composable<SaleRoute.TransactionStatus> {
            BackHandler {
                navController.navigateToHome()
            }

            SetStatusBarColor(PrimaryColor)

            val viewModel = it.sharedViewModel<SaleViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            TransactionScreenReceipt(
                title = uiState.title,
                amount = uiState.amount,
                dateTime = uiState.transactionDateTime,
                content = {
                    SaleScreenReceiptScreen(
                        uiState.maskedCardNumber,
                        uiState.aidName,
                        viewModel.getMerchantName(),
                        viewModel.getTid(),
                        viewModel.getTraceNumber().toString(),
                        uiState.refNo,
                        StringUtil.formatRupiahCurrency(uiState.tip),
                        StringUtil.formatRupiahCurrency(uiState.amount),
                        StringUtil.formatRupiahCurrency((uiState.amount.toLong() + uiState.tip.toLong()).toString()),
                    )
                },
                onEmail = { },
                onBackToHome = { navController.navigateToHome() },
                onPrint = { viewModel.printReceipt() }
            )
        }
    }
}