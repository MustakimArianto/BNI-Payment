package id.co.integrapratama.bnipayment.feature_tap_cash

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.feature_settlement.SettlementViewModel
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.settlementNavGraph(navController: NavController) {
    navigation<AppRoute.TapCash>(
        startDestination = TapCashRoute.Menu
    ) {
        composable<TapCashRoute.Menu> {
            val viewModel = it.sharedViewModel<SettlementViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()


        }
    }
}