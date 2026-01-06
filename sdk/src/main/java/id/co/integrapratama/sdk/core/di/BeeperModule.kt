package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.beeper.BeeperUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.beeper.BeeperIngenicoUtility
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.landi.beeper.BeeperLandiUtility
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.sunmi.beeper.BeeperSunmiUtility
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import id.co.payment2go.terminalsdkhelper.verifone.beeper.BeeperVerifoneUtility
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BeeperModule {

    @Singleton
    @Provides
    fun provideBeeperUtility(
        bindService: BindService,
        deviceTypeManager: DeviceTypeManager
    ): BeeperUtility {
        when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> {
                bindService as BindServiceIngenico
                return BeeperIngenicoUtility(bindService)
            }

            DeviceType.SUNMI -> {
                bindService as BindServiceSunmi
                return BeeperSunmiUtility(bindService)
            }

            DeviceType.VERIFONE -> {
                bindService as BindServiceVerifone
                return BeeperVerifoneUtility(bindService)
            }

            DeviceType.LANDI -> {
                bindService as BindServiceLandi
                return BeeperLandiUtility(bindService)
            }
        }
    }
}