package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.led.LedUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.led.LEDIngenicoUtility
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.landi.led.LEDLandiUtility
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.sunmi.led.LEDSunmiUtility
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import id.co.payment2go.terminalsdkhelper.verifone.led.LEDVerifoneUtility
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LedModule {

    @Singleton
    @Provides
    fun provideLedUtility(
        bindService: BindService,
        deviceTypeManager: DeviceTypeManager
    ): LedUtility {
        when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> {
                bindService as BindServiceIngenico
                return LEDIngenicoUtility(bindService)
            }

            DeviceType.SUNMI -> {
                bindService as BindServiceSunmi
                return LEDSunmiUtility(bindService)
            }

            DeviceType.VERIFONE -> {
                bindService as BindServiceVerifone
                return LEDVerifoneUtility(bindService)
            }

            DeviceType.LANDI -> {
                bindService as BindServiceLandi
                return LEDLandiUtility(bindService)
            }
        }
    }
}