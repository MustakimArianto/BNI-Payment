package id.co.integrapratama.sdk.feature_sale.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.feature_sale.data.SaleRepositoryImpl
import id.co.integrapratama.sdk.feature_sale.domain.SaleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SaleModule {
    @Provides
    @Singleton
    fun provideSaleRepository(
        isoRepository: Iso8583Repository,
        appDatabase: AppDatabase,
    ): SaleRepository = SaleRepositoryImpl(
        isoRepository = isoRepository,
        db = appDatabase
    )
}