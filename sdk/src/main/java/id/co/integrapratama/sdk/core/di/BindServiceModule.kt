package id.co.integrapratama.sdk.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BindServiceModule {

    @Provides
    @Singleton
    fun provideBindService(
        @ApplicationContext context: Context,
        deviceTypeManager: DeviceTypeManager
    ): BindService {
        return when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> BindServiceIngenico(context)
            DeviceType.SUNMI -> BindServiceSunmi(context)
            DeviceType.VERIFONE -> BindServiceVerifone(context)
            DeviceType.LANDI -> BindServiceLandi(
                context,
                deviceTypeManager
            )
        }
    }
}