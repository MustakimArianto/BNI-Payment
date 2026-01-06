package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.sam_reader.SamReaderUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.sam_reader.SamReaderUtilityIngenico
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.landi.sam_reader.SamReaderUtilityLandi
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.sunmi.sam_reader.SamReaderUtilitySunmi
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import id.co.payment2go.terminalsdkhelper.verifone.sam_reader.SamReaderUtilityVerifone
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SamReaderModule {
    @Provides
    @Singleton
    fun provideSamReaderUtility(
        bindService: BindService,
        deviceTypeManager: DeviceTypeManager
    ): SamReaderUtility {
        return when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> {
                bindService as BindServiceIngenico
                SamReaderUtilityIngenico(bindService)
            }

            DeviceType.SUNMI -> {
                bindService as BindServiceSunmi
                SamReaderUtilitySunmi(bindService)
            }

            DeviceType.VERIFONE -> {
                bindService as BindServiceVerifone
                SamReaderUtilityVerifone(bindService)
            }

            DeviceType.LANDI -> {
                bindService as BindServiceLandi
                SamReaderUtilityLandi(bindService)
            }
        }
    }
}