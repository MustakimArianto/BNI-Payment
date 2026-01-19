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
import id.co.integrapratama.sdk.feature_installment.data.InstallmentRepositoryImpl
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentRepository
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.integrapratama.sdk.feature_settlement.data.SettlementRepositoryImpl
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementRepository
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettlementModule {
    @Provides
    @Singleton
    fun provideSettlementRepository(
        isoRepository: Iso8583Repository,
        printRepository: PrintRepository,
        db: AppDatabase,
        traceNumberManager: TraceNumberManager,
        batchManager: TerminalBatchManager,
        stanManager: StanManager,
    ): SettlementRepository = SettlementRepositoryImpl(
        isoRepository = isoRepository,
        printRepository = printRepository,
        db = db,
        traceNumberManager = traceNumberManager,
        batchManager = batchManager,
        stanManager = stanManager,
    )
}