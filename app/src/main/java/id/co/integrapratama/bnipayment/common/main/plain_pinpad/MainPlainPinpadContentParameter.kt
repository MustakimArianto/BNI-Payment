package id.co.integrapratama.bnipayment.common.main.plain_pinpad

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

class MainPlainPinpadContentParameter(
    val navBackStackEntry: NavBackStackEntry,
    val enableInputPinScope: @Composable (@Composable (InputPinScopeParameter) -> Unit) -> Unit
)