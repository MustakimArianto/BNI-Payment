package id.co.integrapratama.bnipayment.feature_void

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import id.co.integrapratama.bnipayment.common.ext.camelCase
import id.co.integrapratama.bnipayment.common.ext.navigateFromCurrent
import id.co.integrapratama.bnipayment.common.ext.navigateToHome
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.maskCardNumber
import id.co.integrapratama.bnipayment.common.ui_component.ErrorDialog
import id.co.integrapratama.bnipayment.common.ui_component.LoadingDialog
import id.co.integrapratama.bnipayment.feature_merchant_pin.merchantPinComposable
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.voidNavigation(navController: NavController) {
    navigation<AppRoute.Void>(
        startDestination = VoidRoute.InputMerchantPin
    ) {
        merchantPinComposable<VoidRoute.InputMerchantPin>(
            navController = navController
        ) { contentParam ->
            contentParam.renderPinInput { pinScope ->
                val callbacks = pinScope.callbacks

                callbacks.setTitle { titleSetter ->
                    titleSetter.invoke("Void")
                }

                callbacks.onAccept { acceptHandler ->
                    acceptHandler.invoke {
                        navController.navigateFromCurrent(
                            VoidRoute.InputTraceNo,
                            isInclusive = true
                        )
                    }
                }

                callbacks.onCancel { cancelHandler ->
                    cancelHandler.invoke {
                        navController.navigateToHome()
                    }
                }
            }
        }

        composable<VoidRoute.InputTraceNo> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<VoidViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.hasConfirmVoid) {
                if (uiState.hasConfirmVoid) {
                    navController.navigateFromCurrent(VoidRoute.ConfirmVoid, isInclusive = true)
                }
            }

            VoidInputTraceNoScreen(
                traceNo = uiState.traceNo,
                onTraceNoChanged = { traceNo ->
                    viewModel.onEvent(VoidUiEvent.OnTraceNoChange(traceNo))
                },
                transactionList = uiState.transactionList,
                onTraceClick = {
                    if (uiState.traceNo.isNotEmpty()) {
                        viewModel.onEvent(VoidUiEvent.ConfirmVoid)
                    } else {
                        viewModel.onEvent(
                            VoidUiEvent.SetErrorMessage("Trace number tidak boleh kosong")
                        )
                    }
                },
                onTransactionItemClick = { transaction ->
                    viewModel.onEvent(VoidUiEvent.OnTraceNoChange(transaction.invoice))
                    viewModel.onEvent(VoidUiEvent.TransactionListClicked(transaction))
                }
            )

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = "Void",
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onButtonClicked = { viewModel.clearErrorMessage() }
                )
            }
        }

        composable<VoidRoute.ConfirmVoid> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<VoidViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.isTransactionFinished) {
                if (uiState.isTransactionFinished) {
                    navController.navigateFromCurrent(VoidRoute.TransactionStatus, isInclusive = true)
                }
            }

            if (uiState.isLoading && uiState.loadingMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.loadingMessage)
            }

            if (uiState.errorMessage.isNotEmpty()) {
                ErrorDialog(
                    title = "Void",
                    message = uiState.errorMessage,
                    textButton = "Ok",
                    onButtonClicked = { navController.navigateToHome() }
                )
            }

            val amount = uiState.currentVoidTransaction!!.amount
            val tip = uiState.currentVoidTransaction!!.tip


            VoidConfirmTransactionScreen(
                transaction = uiState.currentVoidTransaction!!,
                maskedCardNo = maskCardNumber(uiState.currentVoidTransaction!!.cardNo),
                cardName = uiState.currentVoidTransaction!!.customerName,
                transactionDate = uiState.currentVoidTransaction!!.invoiceDate,
                transactionTime = uiState.currentVoidTransaction!!.invoiceDate,
                refNo = uiState.currentVoidTransaction!!.refNo,
                mid = viewModel.getMid(),
                method = "${uiState.currentVoidTransaction!!.cardClassification.camelCase()} Card",
                amount = amount.toString(),
                tip = tip.toString(),
                traceNo = uiState.traceNo,
                totalAmount = (amount + tip).toString(),
                onVoidTransaction = {
                    viewModel.onEvent(VoidUiEvent.SubmitVoid)
                })
        }

        composable<VoidRoute.TransactionStatus> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<VoidViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            fun navigateToHome() {
                navController.navigateToHome()
            }

            BackHandler {
                navigateToHome()
            }

            if (uiState.isLoading && uiState.loadingMessage.isNotEmpty()) {
                LoadingDialog(message = uiState.loadingMessage)
            }

            VoidTransactionStatus(
                transactionResultMessage = uiState.transactionResultMessage,
                onGoToHome = { navigateToHome() },
                onPrintReceipt = {
                    viewModel.onEvent(VoidUiEvent.PrintReceipt)
                }
            )
        }
    }
}