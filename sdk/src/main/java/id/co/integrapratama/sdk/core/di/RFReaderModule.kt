package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.rf_reader.RFReaderUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.rf_reader.RFReaderUtilityIngenico
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.landi.rf_reader.RFReaderUtilityLandi
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.sunmi.rf_reader.RFReaderUtilitySunmi
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import id.co.payment2go.terminalsdkhelper.verifone.rf_reader.RFReaderUtilityVerifone
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RFReaderModule {

    @Provides
    @Singleton
    fun provideRfReaderUtility(
        bindService: BindService,
        deviceTypeManager: DeviceTypeManager
    ): RFReaderUtility {
        return when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> {
                bindService as BindServiceIngenico
                RFReaderUtilityIngenico(bindService)
            }

            DeviceType.SUNMI -> {
                bindService as BindServiceSunmi
                RFReaderUtilitySunmi(bindService)
            }

            DeviceType.VERIFONE -> {
                bindService as BindServiceVerifone
                RFReaderUtilityVerifone(bindService)
            }

            DeviceType.LANDI -> {
                bindService as BindServiceLandi
                RFReaderUtilityLandi(bindService)
            }
        }
    }
}