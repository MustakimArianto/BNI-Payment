package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.system.device.DeviceManagerUtilityIngenico
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.landi.system.device.DeviceManagerUtilityLandi
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.sunmi.system.device.DeviceManagerUtilitySunmi
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import id.co.payment2go.terminalsdkhelper.verifone.system.DeviceManagerUtilityVerifone
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemModule {


    @Provides
    @Singleton
    fun provideDeviceManagerUtility(
        bindService: BindService,
        deviceTypeManager: DeviceTypeManager
    ): DeviceManagerUtility {
        return when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> {
                bindService as BindServiceIngenico
                DeviceManagerUtilityIngenico(bindService)
            }

            DeviceType.SUNMI -> {
                bindService as BindServiceSunmi
                DeviceManagerUtilitySunmi(bindService)
            }

            DeviceType.VERIFONE -> {
                bindService as BindServiceVerifone
                DeviceManagerUtilityVerifone(bindService)
            }

            DeviceType.LANDI -> {
                bindService as BindServiceLandi
                DeviceManagerUtilityLandi(bindService)
            }
        }
    }
}
