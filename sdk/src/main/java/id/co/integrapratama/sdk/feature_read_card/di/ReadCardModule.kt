package id.co.integrapratama.sdk.feature_read_card.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.feature_read_card.data.ReadCardRepositoryImpl
import id.co.integrapratama.sdk.feature_read_card.domain.ReadCardRepository
import id.co.payment2go.terminalsdkhelper.common.emv.EMVUtility
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReadCardModule {

    @Provides
    @Singleton
    fun provideReadCardRepository(
        appDatabase: AppDatabase, emvUUtility: EMVUtility, stanManager: StanManager
    ): ReadCardRepository = ReadCardRepositoryImpl(appDatabase, emvUUtility, stanManager)

}