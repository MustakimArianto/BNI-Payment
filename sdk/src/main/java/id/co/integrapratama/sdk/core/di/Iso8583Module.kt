package id.co.integrapratama.sdk.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.iso8583.IsoSocketClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Iso8583Module {

    @Provides
    @Singleton
    fun provideIsoSocketClient(): IsoSocketClient =
        IsoSocketClient(host = "192.168.6.93", port = 5000)

    @Provides
    @Singleton
    fun provideIso8583Repository(
        client: IsoSocketClient
    ): Iso8583Repository = Iso8583Repository(client)
}