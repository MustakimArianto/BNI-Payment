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
        ) {
            val viewModel = it.navBackStackEntry.sharedViewModel<SettlementViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            it.enableMerchantInputPinScope { it2 ->
                val action = it2.action

                action.enableSetTitleScope { it3 ->
                    it3.enableLaunchScope("Settlement")
                }

                action.enableOnAcceptPinScope { it3 ->
                    it3.enableLaunchScope {
                        navController.navigateFromCurrent(
                            SettlementRoute.TotalSettlement,
                            isInclusive = true
                        )
                    }
                }

                action.enableOnCancelPinScope { it3 ->
                    it3.enableLaunchScope {
                        navController.navigateToHome()
                    }
                }
            }
        }

        composable<SettlementRoute.TotalSettlement> {
            val viewModel = it.sharedViewModel<SettlementViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        }
    }
}