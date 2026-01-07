package id.co.integrapratama.sdk.feature_bin_range.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.feature_bin_range.data.BinRangeRepositoryImpl
import id.co.integrapratama.sdk.feature_bin_range.domain.BinRangeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BinRangeModule {
    @Provides
    @Singleton
    fun provideBinRangeRepository(db: AppDatabase): BinRangeRepository {
        return BinRangeRepositoryImpl(db)
    }
}