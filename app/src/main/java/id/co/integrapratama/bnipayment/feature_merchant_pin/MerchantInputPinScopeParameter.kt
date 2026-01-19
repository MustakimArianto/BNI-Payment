package id.co.integrapratama.bnipayment.feature_merchant_pin

import androidx.compose.runtime.Composable

class MerchantInputPinScopeParameter(
    val pin: String,
    val action: MerchantInputPinAction,
)

class MerchantInputPinAction(
    val enableSetTitleScope: @Composable (@Composable (MerchantPinEnableSetTitleScopeParameter) -> Unit) -> Unit,
    val enableOnAcceptPinScope: @Composable (@Composable (MerchantEnableOnAcceptPinScopeParameter) -> Unit) -> Unit,
    val enableOnCancelPinScope: @Composable (@Composable (MerchantEnableOnCancelPinScopeParameter) -> Unit) -> Unit,
)

class MerchantPinEnableSetTitleScopeParameter(
    val enableLaunchScope: @Composable (String) -> Unit
)

class MerchantEnableOnAcceptPinScopeParameter(
    val enableLaunchScope: @Composable (() -> Unit) -> Unit
)

class MerchantEnableOnCancelPinScopeParameter(
    val enableLaunchScope: @Composable (() -> Unit) -> Unit
)