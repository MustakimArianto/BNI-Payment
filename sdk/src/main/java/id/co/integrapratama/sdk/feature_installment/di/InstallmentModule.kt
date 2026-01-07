package id.co.integrapratama.sdk.feature_installment.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.feature_installment.data.InstallmentRepositoryImpl
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InstallmentModule {
    @Provides
    @Singleton
    fun provideInstallmentRepository(
        isoRepository: Iso8583Repository,
    ): InstallmentRepository = InstallmentRepositoryImpl(
        isoRepository = isoRepository
    )
}