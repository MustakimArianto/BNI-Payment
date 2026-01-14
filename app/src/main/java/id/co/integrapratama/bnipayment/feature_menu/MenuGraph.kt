// MenuGraph.kt
package id.co.integrapratama.bnipayment.feature_menu

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.menuNavigation(navController: NavController) {
    composable<AppRoute.Main> {
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

        MenuScreen()
    }
}