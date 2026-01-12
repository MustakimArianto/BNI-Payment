package id.co.integrapratama.sdk.feature_card_list.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.feature_card_list.data.CardListRepositoryImpl
import id.co.integrapratama.sdk.feature_card_list.data.remote.CardListApi
import id.co.integrapratama.sdk.feature_card_list.domain.CardListRepository
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CardListModule {

    @Provides
    @Singleton
    fun cardListApi(retrofit: Retrofit): CardListApi = retrofit.create()

    @Provides
    @Singleton
    fun provideCardListRepository(
        api: CardListApi, db: AppDatabase, deviceManagerUtility: DeviceManagerUtility
    ): CardListRepository {
        return CardListRepositoryImpl(api, db, deviceManagerUtility)
    }
}