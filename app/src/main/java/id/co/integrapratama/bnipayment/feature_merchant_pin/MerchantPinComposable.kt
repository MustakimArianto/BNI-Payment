package id.co.integrapratama.bnipayment.feature_merchant_pin

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.common.main.plain_pinpad.mainPlainPinpadComposable
import id.co.integrapratama.bnipayment.common.ui_component.LoadingDialog
import id.co.integrapratama.bnipayment.common.ui_component.SetStatusBarColor
import id.co.integrapratama.bnipayment.ui.theme.PrimaryColor
import kotlin.reflect.KType

inline fun <reified T : Any> NavGraphBuilder.merchantPinComposable(
    navController: NavController,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = null,
    noinline exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = null,
    noinline popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = enterTransition,
    noinline popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = exitTransition,
    noinline sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)? = null,
    noinline content: @Composable AnimatedContentScope.(MerchantPinContentParameter) -> Unit
) {
    mainPlainPinpadComposable<T>(
        navController = navController,
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        sizeTransform = sizeTransform
    ) { pinpadParam ->
        val viewModel = pinpadParam.navBackStackEntry.sharedViewModel<MerchantPinViewModel>(navController)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        SetStatusBarColor(PrimaryColor)

        content(
            MerchantPinContentParameter(
                navBackStackEntry = pinpadParam.navBackStackEntry,
                renderPinInput = { merchantPinContent ->
                    pinpadParam.enableInputPinScope { pinInputScope ->
                        val pinCallbacks = pinInputScope.action

                        merchantPinContent(
                            MerchantPinScope(
                                pin = pinInputScope.pin,
                                callbacks = MerchantPinCallbacks(
                                    setTitle = { titleCallback ->
                                        titleCallback(
                                            TitleSetter(
                                                invoke = { title ->
                                                    pinCallbacks.enableSetTitleScope { titleScope ->
                                                        titleScope.enableLaunchScope(title)
                                                    }
                                                }
                                            )
                                        )
                                    },
                                    onAccept = { acceptCallback ->
                                        acceptCallback(
                                            AcceptHandler(
                                                invoke = { handler ->
                                                    pinCallbacks.enableOnAcceptPinScope { acceptScope ->
                                                        acceptScope.enableLaunchScope(handler)
                                                    }
                                                }
                                            )
                                        )
                                    },
                                    onCancel = { cancelCallback ->
                                        cancelCallback(
                                            CancelHandler(
                                                invoke = { handler ->
                                                    pinCallbacks.enableOnCancelPinScope { cancelScope ->
                                                        cancelScope.enableLaunchScope(handler)
                                                    }
                                                }
                                            )
                                        )
                                    }
                                )
                            )
                        )

                        pinCallbacks.enableSetDescriptionScope { descScope ->
                            descScope.enableLaunchScope("Enter Merchant PIN")
                        }

                        pinCallbacks.enableOnPreCheckPinScope { preCheckScope ->
                            LaunchedEffect(uiState.checkingMerchantPinStatus) {
                                when (uiState.checkingMerchantPinStatus) {
                                    1 -> preCheckScope.accept()
                                    0 -> preCheckScope.decline()
                                }
                            }

                            preCheckScope.enableLaunchScope {
                                viewModel.onEvent(
                                    MerchantPinUiEvent.ChangeMerchantPin(pin = pinInputScope.pin)
                                )
                                viewModel.onEvent(MerchantPinUiEvent.SubmitMerchantPin)
                            }
                        }

                        if (uiState.loadingMessage.isNotEmpty()) {
                            LoadingDialog(message = uiState.loadingMessage)
                        }
                    }
                }
            )
        )
    }
}