package id.co.integrapratama.bnipayment.common.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

/**
 * Extension function to get a shared ViewModel scoped to the parent navigation graph
 * Automatically detects the parent route from the current destination
 *
 * Usage:
 * val viewModel: SaleViewModel = backStackEntry.sharedViewModel(navController)
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(viewModelStoreOwner = parentEntry)
}