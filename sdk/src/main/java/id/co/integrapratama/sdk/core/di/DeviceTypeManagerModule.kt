package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceTypeManagerModule {
    @Provides
    @Singleton
    fun provideDeviceTypeManager(): DeviceTypeManager = DeviceTypeManager()
}