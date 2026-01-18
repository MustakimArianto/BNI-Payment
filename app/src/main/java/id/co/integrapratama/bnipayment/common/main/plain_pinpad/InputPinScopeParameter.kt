package id.co.integrapratama.bnipayment.common.main.plain_pinpad

import androidx.compose.runtime.Composable

class InputPinScopeParameter(
    val pin: String,
    val inputPlainPinpadPhase: InputPlainPinpadPhase,
    val action: InputPinAction,
)

class InputPinAction(
    val enableSetTitleScope: @Composable (@Composable (EnableSetTitleScopeParameter) -> Unit) -> Unit,
    val enableSetDescriptionScope: @Composable (@Composable (EnableSetDescriptionScopeParameter) -> Unit) -> Unit,
    val enableSetPinpadCancelConfigScope: @Composable (@Composable (EnableSetPinpadCancelConfigScopeParameter) -> Unit) -> Unit,
    val enableOnPreCheckPinScope: @Composable (@Composable (EnableOnPreCheckPinScopeParameter) -> Unit) -> Unit,
    val enableOnCheckPinScope: @Composable (@Composable (EnableOnCheckPinScopeParameter) -> Unit) -> Unit,
    val enableOnAcceptPinScope: @Composable (@Composable (EnableOnAcceptPinScopeParameter) -> Unit) -> Unit,
    val enableOnDeclinePinScope: @Composable (@Composable (EnableOnDeclinePinScopeParameter) -> Unit) -> Unit,
    val enableOnCancelPinScope: @Composable (@Composable (EnableOnCancelPinScopeParameter) -> Unit) -> Unit,
)

class EnableSetTitleScopeParameter(
    val enableLaunchScope: @Composable (String) -> Unit
)

class EnableSetDescriptionScopeParameter(
    val enableLaunchScope: @Composable (String) -> Unit
)

class EnableSetPinpadCancelConfigScopeParameter(
    val enableLaunchScope: @Composable (InputPlainPinpadCancelConfig) -> Unit
)

class EnableOnPreCheckPinScopeParameter(
    val checking: () -> Unit,
    val accept: () -> Unit,
    val decline: () -> Unit,
    val enableLaunchScope: @Composable (() -> Unit) -> Unit
)

class EnableOnCheckPinScopeParameter(
    val enableLaunchScope: @Composable (() -> Unit) -> Unit
)

class EnableOnAcceptPinScopeParameter(
    val enableLaunchScope: @Composable (() -> Unit) -> Unit
)

class EnableOnDeclinePinScopeParameter(
    val enableLaunchScope: @Composable (() -> Unit) -> Unit
)

class EnableOnCancelPinScopeParameter(
    val enableLaunchScope: @Composable (() -> Unit) -> Unit
)