package id.co.integrapratama.sdk.core.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.pinpad.PinpadUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.pinpad.PinpadIngenicoUtility
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.landi.pinpad.PinpadLandiUtility
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.sunmi.pinpad.PinpadSunmiUtility
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import id.co.payment2go.terminalsdkhelper.verifone.pinpad.PinpadVerifoneUtility
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PinpadModule {

    @Provides
    @Singleton
    fun providePinpadUtility(
        @ApplicationContext context: Context,
        deviceTypeManager: DeviceTypeManager,
        bindService: BindService
    ): PinpadUtility {
        return when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> PinpadIngenicoUtility(context, bindService as BindServiceIngenico)
            DeviceType.SUNMI -> PinpadSunmiUtility(bindService as BindServiceSunmi)
            DeviceType.VERIFONE -> PinpadVerifoneUtility(bindService as BindServiceVerifone)
            DeviceType.LANDI -> PinpadLandiUtility(bindService as BindServiceLandi)
        }
    }
}