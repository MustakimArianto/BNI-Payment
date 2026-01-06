package id.co.integrapratama.sdk.feature_capk_master.di

import id.co.integrapratama.sdk.core.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.feature_capk_master.data.CapkMasterRepositoryImpl
import id.co.integrapratama.sdk.feature_capk_master.data.remote.CapkMasterApi
import id.co.integrapratama.sdk.feature_capk_master.domain.CapkMasterRepository
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CapkMasterModule {
    @Provides
    @Singleton
    fun provideCapkMasterApi(retrofit: Retrofit): CapkMasterApi = retrofit.create()

    @Provides
    @Singleton
    fun provideCapkMasterRepository(
        capkMasterApi: CapkMasterApi,
        appDatabase: AppDatabase,
        deviceManagerUtility: DeviceManagerUtility
    ): CapkMasterRepository {
        return CapkMasterRepositoryImpl(capkMasterApi, appDatabase, deviceManagerUtility)
    }
}