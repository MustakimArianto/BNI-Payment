package id.co.integrapratama.bnipayment.feature_merchant_pin

import androidx.compose.runtime.Composable

class MerchantPinScope(
    val pin: String,
    val callbacks: MerchantPinCallbacks,
)

class MerchantPinCallbacks(
    val setTitle: @Composable (@Composable (TitleSetter) -> Unit) -> Unit,
    val onAccept: @Composable (@Composable (AcceptHandler) -> Unit) -> Unit,
    val onCancel: @Composable (@Composable (CancelHandler) -> Unit) -> Unit,
)

class TitleSetter(
    val invoke: @Composable (String) -> Unit
)

class AcceptHandler(
    val invoke: @Composable (() -> Unit) -> Unit
)

class CancelHandler(
    val invoke: @Composable (() -> Unit) -> Unit
)