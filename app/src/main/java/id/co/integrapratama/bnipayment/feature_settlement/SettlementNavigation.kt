package id.co.integrapratama.bnipayment.feature_settlement

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import id.co.integrapratama.bnipayment.common.ext.navigateFromCurrent
import id.co.integrapratama.bnipayment.common.ext.navigateToHome
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.feature_merchant_pin.merchantPinComposable
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.settlementNavGraph(navController: NavController) {
    navigation<AppRoute.Settlement>(
        startDestination = SettlementRoute.InputMerchantPin
    ) {
        merchantPinComposable<SettlementRoute.InputMerchantPin>(
            navController = navController
        ) { contentParam ->
            val viewModel = contentParam.navBackStackEntry.sharedViewModel<SettlementViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            contentParam.renderPinInput { pinScope ->
                val callbacks = pinScope.callbacks

                callbacks.setTitle { titleSetter ->
                    titleSetter.invoke("Settlement")
                }

                callbacks.onAccept { acceptHandler ->
                    acceptHandler.invoke {
                        navController.navigateFromCurrent(
                            SettlementRoute.TotalSettlement,
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

        composable<SettlementRoute.TotalSettlement> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<SettlementViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            // TODO: Add TotalSettlement screen content
        }
    }
}