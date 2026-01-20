package id.co.integrapratama.bnipayment.common.main.plain_pinpad

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import id.co.integrapratama.bnipayment.common.ext.copyWith
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.ui_component.CustomPinpad
import id.co.integrapratama.bnipayment.common.ui_component.CustomPinpadType
import id.co.integrapratama.bnipayment.common.ui_component.OnlyPinpadButtonBehavior
import id.co.integrapratama.bnipayment.common.ui_component.PinUnderlineView
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import kotlin.reflect.KType

inline fun <reified T : Any> NavGraphBuilder.mainPlainPinpadComposable(
    navController: NavController,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = null,
    noinline exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = null,
    noinline popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = enterTransition,
    noinline popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = exitTransition,
    noinline sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)? = null,
    noinline content: @Composable AnimatedContentScope.(MainPlainPinpadContentParameter) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        sizeTransform = sizeTransform,
    ) {
        val viewModel = it.sharedViewModel<MainPlainPinpadViewModel>(navController)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val inputPhase = uiState.inputPhase
        LaunchedEffect(inputPhase) {
            if (inputPhase is InputPlainPinpadPhase.DeclinePhase) {
                viewModel.onEvent(
                    MainPlainPinpadUiEvent.SetAbleToTap(false)
                )
            }
        }

        val mainPlainPinpadContentParameter = MainPlainPinpadContentParameter(
            navBackStackEntry = it,
            enableInputPinScope = { it2 ->
                it2(
                    InputPinScopeParameter(
                        pin = uiState.pin,
                        inputPlainPinpadPhase = uiState.inputPhase,
                        action = InputPinAction(
                            enableSetTitleScope = { it3 ->
                                it3(
                                    EnableSetTitleScopeParameter(
                                        enableLaunchScope = { it4 ->
                                            LaunchedEffect(Unit) {
                                                viewModel.onEvent(
                                                    MainPlainPinpadUiEvent.SetTitle(it4)
                                                )
                                            }
                                        }
                                    )
                                )
                            },
                            enableSetDescriptionScope = { it3 ->
                                it3(
                                    EnableSetDescriptionScopeParameter(
                                        enableLaunchScope = { it4 ->
                                            LaunchedEffect(Unit) {
                                                viewModel.onEvent(
                                                    MainPlainPinpadUiEvent.SetDescription(it4)
                                                )
                                            }
                                        }
                                    )
                                )
                            },
                            enableSetPinpadCancelConfigScope = { it3 ->
                                it3(
                                    EnableSetPinpadCancelConfigScopeParameter(
                                        enableLaunchScope = { it4 ->
                                            LaunchedEffect(Unit) {
                                                viewModel.onEvent(
                                                    MainPlainPinpadUiEvent.SetPinpadCancelConfig(it4)
                                                )
                                            }
                                        }
                                    )
                                )
                            },
                            enableOnPreCheckPinScope = { it3 ->
                                it3(
                                    EnableOnPreCheckPinScopeParameter(
                                        checking = {
                                            viewModel.onEvent(
                                                MainPlainPinpadUiEvent.UpdateInputPlainPinpadPhase(
                                                    InputPlainPinpadPhase.CheckingPhase
                                                )
                                            )
                                        },
                                        accept = {
                                            viewModel.onEvent(
                                                MainPlainPinpadUiEvent.UpdateInputPlainPinpadPhase(
                                                    InputPlainPinpadPhase.AcceptPhase
                                                )
                                            )
                                        },
                                        decline = {
                                            viewModel.onEvent(
                                                MainPlainPinpadUiEvent.UpdateInputPlainPinpadPhase(
                                                    InputPlainPinpadPhase.DeclinePhase
                                                )
                                            )
                                        },
                                        enableLaunchScope = { it4 ->
                                            LaunchedEffect(inputPhase) {
                                                if (inputPhase is InputPlainPinpadPhase.PreCheckingPhase) {
                                                    it4()
                                                }
                                            }
                                        }
                                    )
                                )
                            },
                            enableOnCheckPinScope = { it3 ->
                                it3(
                                    EnableOnCheckPinScopeParameter(
                                        enableLaunchScope = { it4 ->
                                            LaunchedEffect(inputPhase) {
                                                if (inputPhase is InputPlainPinpadPhase.CheckingPhase) {
                                                    it4()
                                                }
                                            }
                                        }
                                    )
                                )
                            },
                            enableOnAcceptPinScope = { it3 ->
                                it3(
                                    EnableOnAcceptPinScopeParameter(
                                        enableLaunchScope = { it4 ->
                                            LaunchedEffect(inputPhase) {
                                                if (inputPhase is InputPlainPinpadPhase.AcceptPhase) {
                                                    it4()
                                                }
                                            }
                                        }
                                    )
                                )
                            },
                            enableOnDeclinePinScope = { it3 ->
                                it3(
                                    EnableOnDeclinePinScopeParameter(
                                        enableLaunchScope = { it4 ->
                                            LaunchedEffect(inputPhase) {
                                                if (inputPhase is InputPlainPinpadPhase.DeclinePhase) {
                                                    it4()
                                                }
                                            }
                                        }
                                    )
                                )
                            },
                            enableOnCancelPinScope = { it3 ->
                                it3(
                                    EnableOnCancelPinScopeParameter(
                                        enableLaunchScope = { it4 ->
                                            LaunchedEffect(inputPhase) {
                                                if (inputPhase is InputPlainPinpadPhase.CancelPhase) {
                                                    it4()
                                                }
                                            }
                                        }
                                    )
                                )
                            },
                        )
                    )
                )
            }
        )

        content(mainPlainPinpadContentParameter)

        Column {
            BackHandler {
                val pinpadCancelConfig = uiState.pinpadCancelConfig
                viewModel.onEvent(
                    MainPlainPinpadUiEvent.UpdateInputPlainPinpadPhase(
                        InputPlainPinpadPhase.CancelPhase(
                            withDisableTap = pinpadCancelConfig?.withDisableTap ?: true
                        )
                    )
                )
            }

            Spacer(Modifier.height(16.dp))
            TopBar(
                title = uiState.title,
            )
            VerticalSpacer(SpacerSize.X_LARGE)

            Column(
                modifier = Modifier
                    .padding(PaddingValues(16.dp).copyWith(top = 0.dp))
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = uiState.description.ifEmpty { "Enter PIN" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(30.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PinUnderlineView(
                        pinLength = uiState.pinMaxLength,
                        value = uiState.pin
                    )

                    if (inputPhase is InputPlainPinpadPhase.DeclinePhase) {
                        Spacer(Modifier.height(30.dp))

                        Text(
                            text = "PIN has incorrect",
                            fontSize = 14.sp,
                            color = Color.Red
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.wrapContentHeight()
            ) {
                CustomPinpad(
                    isPhysicalKeyboard = uiState.isPhysicalKeyboard,
                    type = CustomPinpadType.OnlyPinpadButton(
                        OnlyPinpadButtonBehavior.OnPinpadInputBasedEventBehavior(
                            pinLength = uiState.pinMaxLength,
                            onEvent = { it3 ->
                                viewModel.onEvent(
                                    MainPlainPinpadUiEvent.OnPinpadInputBasedEvent(it3)
                                )
                            }
                        )
                    ),
                )
                if (uiState.ableToTap) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .then(
                                Modifier.pointerInput(Unit) {
                                    detectTapGestures { }
                                }
                            )
                    )
                }
            }
        }
    }
}