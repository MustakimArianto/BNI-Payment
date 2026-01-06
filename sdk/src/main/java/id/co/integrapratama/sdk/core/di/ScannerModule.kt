package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.scanner.ScannerUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.scanner.ScannerIngenicoUtility

@Module
@InstallIn(ViewModelComponent::class)
object ScannerModule {


    @Provides
    @ViewModelScoped
    fun provideScannerUtility(
        bindService: BindService,
        deviceTypeManager: DeviceTypeManager
    ): ScannerUtility {
        return when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> {
                bindService as BindServiceIngenico
                ScannerIngenicoUtility(bindService)
            }

            DeviceType.SUNMI -> {
                TODO("Not yet implemented")
            }

            DeviceType.VERIFONE -> {
                TODO("Not yet implemented")
            }

            DeviceType.LANDI -> {
                TODO("Not yet implemented")
            }
        }
    }
}
