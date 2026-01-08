// MainGraph.kt
package id.co.integrapratama.bnipayment.feature_menu

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    composable<AppRoute.Main> {
        val context = LocalContext.current

        BackHandler {
            (context as? Activity)?.finish()
        }

        MainMenuScreen(
            onNavigateToMiniATM = { navController.navigate(AppRoute.MiniATM) },
            onNavigateToSale = { navController.navigate(AppRoute.Sale(isContactless = false)) },
            onNavigateToContactlessSale = { navController.navigate(AppRoute.Sale(isContactless = true)) },
            onNavigateToVoid = { navController.navigate(AppRoute.Void) },
            onNavigateToSettlement = { navController.navigate(AppRoute.Settlement) }
        )
    }
}