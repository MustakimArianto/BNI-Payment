package id.co.integrapratama.bnipayment.feature_admin

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.common.ext.navigateToHome
import id.co.integrapratama.bnipayment.common.ext.sharedViewModel
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.adminNavigation(navController: NavController) {
    navigation<AppRoute.AdminSetting>(
        startDestination = AdminRoute.Menu
    ) {
        composable<AdminRoute.Menu> {
            val viewModel = it.sharedViewModel<AdminViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            BackHandler {
                navController.navigateToHome()
            }

            val toast = Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT)

            AdminScreen(
                title = uiState.title,
                appVersion = viewModel.getAppVersion(),
                onMerchantInfo = { toast.show() },
                onTestConnection = { toast.show() },
                onTerminalInfo = { toast.show() },
                onReaderLogon = { toast.show() },
                onEcr = { toast.show() },
                onBatchInfo = { toast.show() },
                onSetBatch = { toast.show() },
                onSettingConnection = { toast.show() },
                onEcrH2H = { toast.show() },
                onHostSetup = { toast.show() },
                onPOSExtPort = { toast.show() },
                onDeleteReversal = { toast.show() },
                onLanguage = { toast.show() }
            )
        }
    }
}