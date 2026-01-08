package id.co.integrapratama.sdk.feature_print.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.feature_print.data.PrintRepositoryImpl
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.payment2go.terminalsdkhelper.common.printer.PrinterUtility
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrintModule {
    @Provides
    @Singleton
    fun providePrintRepository(
        printerUtility: PrinterUtility,
        @ApplicationContext context: Context,
    ): PrintRepository = PrintRepositoryImpl(printerUtility, context)
}