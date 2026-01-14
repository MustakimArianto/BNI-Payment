package id.co.integrapratama.bnipayment.common.ui_component

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetStatusBarColor(
    color: Color,
    darkIcons: Boolean = false
) {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as ComponentActivity).window
        window.statusBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = darkIcons
        }
    }
}