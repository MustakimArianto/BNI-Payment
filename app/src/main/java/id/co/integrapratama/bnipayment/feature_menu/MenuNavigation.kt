// MenuNavigation.kt
package id.co.integrapratama.bnipayment.feature_menu

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.menuNavigation(navController: NavController) {
    composable<AppRoute.Main> {
        val viewModel = it.sharedViewModel<MenuViewModel>(navController)
        val isHomeActive by viewModel.isHomeActive.collectAsState()
        val isCheckDefaultMenu by viewModel.isCheckDefaultMenu.collectAsState()
        val context = LocalContext.current
        val view = LocalView.current

        SideEffect {
            val window = (view.context as ComponentActivity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }

        BackHandler {
            (context as? Activity)?.finish()
        }

        MenuScreen(
            isCheckDefaultMenu = isCheckDefaultMenu,
            isHomeActive = isHomeActive,
            onDisableMenuChecking = {
                viewModel.onEvent(MenuEvent.DisableMenuChecking)
            }
        )
    }
}