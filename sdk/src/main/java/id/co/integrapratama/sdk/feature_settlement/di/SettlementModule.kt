package id.co.integrapratama.sdk.feature_settlement.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.feature_bin_range.domain.BinRangeRepository
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.integrapratama.sdk.feature_settlement.data.SettlementRepositoryImpl
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettlementModule {
    @Provides
    @Singleton
    fun provideSettlementRepository(
        isoRepository: Iso8583Repository,
        printRepository: PrintRepository,
        binRangeRepository: BinRangeRepository,
        db: AppDatabase,
        batchManager: TerminalBatchManager,
        stanManager: StanManager,
        traceNumberManager: TraceNumberManager
    ): SettlementRepository = SettlementRepositoryImpl(
        isoRepository = isoRepository,
        printRepository = printRepository,
        binRangeRepository = binRangeRepository,
        db = db,
        batchManager = batchManager,
        stanManager = stanManager,
        traceNumberManager = traceNumberManager
    )
}