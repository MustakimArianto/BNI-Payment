package id.co.integrapratama.sdk.feature_aid_master.di

import id.co.integrapratama.sdk.core.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.feature_aid_master.data.AidMasterRepositoryImpl
import id.co.integrapratama.sdk.feature_aid_master.data.remote.AidMasterApi
import id.co.integrapratama.sdk.feature_aid_master.domain.AidMasterRepository
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AidMasterModule {
    @Provides
    @Singleton
    fun provideAidMasterApi(retrofit: Retrofit): AidMasterApi = retrofit.create()

    @Provides
    @Singleton
    fun provideAidMasterRepository(
        aidMasterApi: AidMasterApi,
        appDatabase: AppDatabase,
        deviceManagerUtility: DeviceManagerUtility
    ): AidMasterRepository {
        return AidMasterRepositoryImpl(aidMasterApi, appDatabase, deviceManagerUtility)
    }
}