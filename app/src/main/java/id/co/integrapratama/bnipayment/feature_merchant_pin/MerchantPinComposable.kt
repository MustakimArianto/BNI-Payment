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
    ) {
        val viewModel = it.navBackStackEntry.sharedViewModel<MerchantPinViewModel>(navController)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        content(
            MerchantPinContentParameter(
                navBackStackEntry = it.navBackStackEntry,
                enableMerchantInputPinScope = { it2 ->
                    it.enableInputPinScope { it3 ->
                        val action = it3.action

                        it2(
                            MerchantInputPinScopeParameter(
                                pin = it3.pin,
                                action = MerchantInputPinAction(
                                    enableSetTitleScope = { it4 ->
                                        it4(
                                            MerchantPinEnableSetTitleScopeParameter(
                                                enableLaunchScope = { it5 ->
                                                    action.enableSetTitleScope { it6 ->
                                                        it6.enableLaunchScope(it5)
                                                    }
                                                }
                                            )
                                        )
                                    },
                                    enableOnAcceptPinScope = { it4 ->
                                        it4(
                                            MerchantEnableOnAcceptPinScopeParameter(
                                                enableLaunchScope = { it5 ->
                                                    action.enableOnAcceptPinScope { it6 ->
                                                        it6.enableLaunchScope(it5)
                                                    }
                                                }
                                            )
                                        )
                                    },
                                    enableOnCancelPinScope = { it4 ->
                                        it4(
                                            MerchantEnableOnCancelPinScopeParameter(
                                                enableLaunchScope = { it5 ->
                                                    action.enableOnCancelPinScope { it6 ->
                                                        it6.enableLaunchScope(it5)
                                                    }
                                                }
                                            )
                                        )
                                    }
                                )
                            )
                        )

                        action.enableSetDescriptionScope { it4 ->
                            it4.enableLaunchScope("Enter Merchant PIN")
                        }

                        action.enableOnPreCheckPinScope { it4 ->
                            LaunchedEffect(uiState.checkingMerchantPinStatus) {
                                if (uiState.checkingMerchantPinStatus == 1) {
                                    it4.accept()
                                } else if (uiState.checkingMerchantPinStatus == 0) {
                                    it4.decline()
                                }
                            }

                            it4.enableLaunchScope {
                                viewModel.onEvent(
                                    MerchantPinUiEvent.ChangeMerchantPin(
                                        pin = it3.pin
                                    )
                                )
                                viewModel.onEvent(
                                    MerchantPinUiEvent.SubmitMerchantPin
                                )
                            }
                        }

                        if (uiState.loadingMessage.isNotEmpty()) {
                            LoadingDialog(
                                message = uiState.loadingMessage,
                            )
                        }
                    }
                }
            )
        )
    }
}