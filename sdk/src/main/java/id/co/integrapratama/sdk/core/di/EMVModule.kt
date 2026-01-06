package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.beeper.BeeperUtility
import id.co.payment2go.terminalsdkhelper.common.emv.EMVUtility
import id.co.payment2go.terminalsdkhelper.common.led.LedUtility
import id.co.payment2go.terminalsdkhelper.common.pinpad.PinpadUtility
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.emv.EMVUtilityIngenico
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.landi.emv.EMVUtilityLandi
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.sunmi.emv.EMVUtilitySunmi
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import id.co.payment2go.terminalsdkhelper.verifone.emv.EMVUtilityVerifone
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EMVModule {

    @Provides
    @Singleton
    fun provideEMVUtility(
        bindService: BindService,
        deviceManagerUtility: DeviceManagerUtility,
        deviceTypeManager: DeviceTypeManager,
        pinpadUtility: PinpadUtility,
        ledUtility: LedUtility,
        beeperUtility: BeeperUtility
    ): EMVUtility {
        return when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> {
                bindService as BindServiceIngenico
                EMVUtilityIngenico(
                    bindService = bindService,
                    deviceManagerUtility = deviceManagerUtility,
                    pinpadUtility = pinpadUtility,
                    ledUtility = ledUtility,
                    beeperUtility = beeperUtility
                )
            }

            DeviceType.SUNMI -> {
                bindService as BindServiceSunmi
                EMVUtilitySunmi(bindService)
            }

            DeviceType.VERIFONE -> {
                bindService as BindServiceVerifone
                EMVUtilityVerifone(bindService)
            }

            DeviceType.LANDI -> {
                bindService as BindServiceLandi
                EMVUtilityLandi(
                    bindService = bindService,
                    deviceManagerUtility = deviceManagerUtility,
                    pinpadUtility = pinpadUtility,
                )
            }
        }
    }
}