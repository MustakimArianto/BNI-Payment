package id.co.integrapratama.sdk.feature_mini_atm.balance_info.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TerminalConfigManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.feature_mini_atm.balance_info.data.MiniATMBalanceInfoRepositoryImpl
import id.co.integrapratama.sdk.feature_mini_atm.balance_info.domain.MiniATMBalanceInfoRepository
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MiniATMBalanceInfoModule {
    @Provides
    @Singleton
    fun providesMiniATMBalanceInfoRepository(
        iso8583Repository: Iso8583Repository,
        traceNumberManager: TraceNumberManager,
        terminalConfigManager: TerminalConfigManager,
        printRepository: PrintRepository,
        terminalBatchManager: TerminalBatchManager
    ): MiniATMBalanceInfoRepository {
        return MiniATMBalanceInfoRepositoryImpl(
            iso8583Repository,
            traceNumberManager,
            terminalConfigManager,
            printRepository,
            terminalBatchManager
        )
    }
}