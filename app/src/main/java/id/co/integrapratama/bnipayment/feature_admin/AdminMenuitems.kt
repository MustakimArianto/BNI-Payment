package id.co.integrapratama.bnipayment.feature_admin

import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.model.MenuItem

fun getAdminMenuItems(
    onMerchantInfo: () -> Unit,
    onTestConnection: () -> Unit,
    onTerminalInfo: () -> Unit,
    onReaderLogon: () -> Unit,
    onEcr: () -> Unit,
    onBatchInfo: () -> Unit,
    onSetBatch: () -> Unit,
    onSettingConnection: () -> Unit,
    onEcrH2H: () -> Unit,
    onHostSetup: () -> Unit,
    onPOSExtPort: () -> Unit,
    onDeleteReversal: () -> Unit,
    onLanguage: () -> Unit
) : List<MenuItem> = listOf(
    MenuItem(
        title = "Merchant Info",
        selectedIcon = R.drawable.ic_admin_merchant_info,
        onClick = onMerchantInfo
    ),
    MenuItem(
        title = "Test Connection",
        selectedIcon = R.drawable.ic_admin_test_connection,
        onClick = onTestConnection
    ),
    MenuItem(
        title = "Terminal Info",
        selectedIcon = R.drawable.ic_admin_terminal_info,
        onClick = onTerminalInfo
    ),
    MenuItem(
        title = "Reader Logon",
        selectedIcon = R.drawable.ic_admin_reader_logon,
        onClick = onReaderLogon
    ),
    MenuItem(
        title = "ECR",
        selectedIcon = R.drawable.ic_admin_ecr,
        onClick = onEcr
    ),
    MenuItem(
        title = "Batch Info",
        selectedIcon = R.drawable.ic_admin_batch_info,
        onClick = onBatchInfo
    ),
    MenuItem(
        title = "Set Batch",
        selectedIcon = R.drawable.ic_admin_set_batch,
        onClick = onSetBatch
    ),
    MenuItem(
        title = "Setting Connection",
        selectedIcon = R.drawable.ic_admin_setting_connection,
        onClick = onSettingConnection
    ),
    MenuItem(
        title = "Setting ECR H2H",
        selectedIcon = R.drawable.ic_admin_merchant_info,
        onClick = onEcrH2H
    ),
    MenuItem(
        title = "Host Setup",
        selectedIcon = R.drawable.ic_admin_host_setup,
        onClick = onHostSetup
    ),
    MenuItem(
        title = "POS Ext Port",
        selectedIcon = R.drawable.ic_port_ext,
        onClick = onPOSExtPort
    ),
    MenuItem(
        title = "Delete Reversal",
        selectedIcon = R.drawable.ic_admin_delete_reversal,
        onClick = onDeleteReversal
    ),
    MenuItem(
        title = "Language",
        selectedIcon = R.drawable.ic_admin_language,
        onClick = onLanguage
    )
)