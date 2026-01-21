package id.co.integrapratama.bnipayment.feature_merchant_pin

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

class MerchantPinContentParameter(
    val navBackStackEntry: NavBackStackEntry,
    val renderPinInput: @Composable (@Composable (MerchantPinScope) -> Unit) -> Unit
)