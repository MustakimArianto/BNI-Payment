package id.co.integrapratama.sdk.feature_void.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.ReversalManager
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.integrapratama.sdk.feature_sale.data.SaleRepositoryImpl
import id.co.integrapratama.sdk.feature_sale.domain.SaleRepository
import id.co.integrapratama.sdk.feature_void.data.VoidRepositoryImpl
import id.co.integrapratama.sdk.feature_void.domain.VoidRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VoidModule {
    @Provides
    @Singleton
    fun provideVoidRepository(
        isoRepository: Iso8583Repository,
        appDatabase: AppDatabase,
        printRepository: PrintRepository,
        traceNumberManager: TraceNumberManager,
        batchManager: TerminalBatchManager,
        reversalManager: ReversalManager,
        stanManager: StanManager
    ): VoidRepository = VoidRepositoryImpl(
        isoRepository,
        appDatabase,
        printRepository,
        traceNumberManager,
        batchManager,
        reversalManager,
        stanManager
    )
}