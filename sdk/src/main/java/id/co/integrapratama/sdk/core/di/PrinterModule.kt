package id.co.integrapratama.sdk.core.di

import android.content.Context
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.common.BindService
import id.co.payment2go.terminalsdkhelper.common.DeviceType
import id.co.payment2go.terminalsdkhelper.common.printer.PrinterErrorDesc
import id.co.payment2go.terminalsdkhelper.common.printer.PrinterUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.ingenico.BindServiceIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.printer.PrinterErrorIngenico
import id.co.payment2go.terminalsdkhelper.ingenico.printer.PrinterUtilityIngenico
import id.co.payment2go.terminalsdkhelper.landi.BindServiceLandi
import id.co.payment2go.terminalsdkhelper.landi.printer.PrinterUtilityLandi
import id.co.payment2go.terminalsdkhelper.sunmi.BindServiceSunmi
import id.co.payment2go.terminalsdkhelper.sunmi.printer.PrinterUtilitySunmi
import id.co.payment2go.terminalsdkhelper.verifone.BindServiceVerifone
import id.co.payment2go.terminalsdkhelper.verifone.printer.PrinterUtilityVerifone
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrinterModule {

    @Provides
    @Singleton
    fun providePrinterErrorDescription(): PrinterErrorDesc = PrinterErrorIngenico()

    @Provides
    @Singleton
    fun providePrinterUtility(
        @ApplicationContext
        context: Context,
        printerErrorDesc: Lazy<PrinterErrorDesc>,
        bindService: BindService,
        deviceTypeManager: DeviceTypeManager
    ): PrinterUtility {
        return when (deviceTypeManager.getDeviceType()) {
            DeviceType.INGENICO -> {
                bindService as BindServiceIngenico
                PrinterUtilityIngenico(
                    context = context,
                    bindService,
                    printerErrorDesc = printerErrorDesc.get() as PrinterErrorIngenico
                )
            }

            DeviceType.SUNMI -> {
                PrinterUtilitySunmi(
                    context = context,
                    bindService = bindService as BindServiceSunmi
                )
            }

            DeviceType.VERIFONE -> {
                PrinterUtilityVerifone(
                    context = context,
                    bindService = bindService as BindServiceVerifone
                )
            }

            DeviceType.LANDI -> {
                PrinterUtilityLandi(
                    context = context,
                    bindService = bindService as BindServiceLandi
                )
            }
        }
    }
}