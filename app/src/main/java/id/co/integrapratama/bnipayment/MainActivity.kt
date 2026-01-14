package id.co.integrapratama.bnipayment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.integrapratama.bnipayment.common.main.dialog.MainDialogViewModel
import id.co.integrapratama.bnipayment.common.main.pinpad.MainPinpadViewModel
import id.co.integrapratama.bnipayment.navigation.AppNavHost
import id.co.integrapratama.bnipayment.ui.theme.BNIPaymentTheme
import id.co.integrapratama.logsdk.LogSdk

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    private val requestLegacyStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.values.any { it }
            if (granted) {
                Log.d(TAG, "Legacy storage permission granted")
                LogSdk.init(applicationContext)
            } else {
                Log.w(TAG, "Legacy storage permission denied")
            }
        }

    private val requestManageStorageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Log.d(TAG, "MANAGE_EXTERNAL_STORAGE permission granted")
                    LogSdk.init(applicationContext)
                } else {
                    Log.w(TAG, "MANAGE_EXTERNAL_STORAGE permission denied")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isStoragePermissionGranted()) {
            Log.d(TAG, "Storage permission already granted")
            LogSdk.init(applicationContext)
        } else {
            Log.d(TAG, "Requesting storage permission...")
            requestStoragePermission()
        }

        enableEdgeToEdge()
        setContent {
            viewModel<MainDialogViewModel>(this@MainActivity)
            viewModel<MainPinpadViewModel>(this@MainActivity)

            BNIPaymentTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val writeGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            writeGranted
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:$packageName")
                }
                requestManageStorageLauncher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                requestManageStorageLauncher.launch(intent)
            }
        } else {
            requestLegacyStoragePermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }
}
