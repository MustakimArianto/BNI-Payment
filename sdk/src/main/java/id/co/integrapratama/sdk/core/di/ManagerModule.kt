package id.co.integrapratama.sdk.core.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.integrapratama.sdk.core.AppManager
import id.co.integrapratama.sdk.core.HostUrlManager
import id.co.integrapratama.sdk.core.LogonManager
import id.co.integrapratama.sdk.core.ReversalManager
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TerminalConfigManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.TransactionManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Provides
    @Singleton
    fun provideAppManager(sharedPreferences: SharedPreferences): AppManager {
        return AppManager(sharedPreferences)
    }
    @Provides
    @Singleton
    fun providesHostUrlManager(sharedPreferences: SharedPreferences): HostUrlManager {
        return HostUrlManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideStanManager(sharedPreferences: SharedPreferences): StanManager {
        return StanManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTerminalManager(sharedPreferences: SharedPreferences): TerminalBatchManager {
        return TerminalBatchManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTraceNumberManager(sharedPreferences: SharedPreferences): TraceNumberManager {
        return TraceNumberManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideReversalManager(sharedPreferences: SharedPreferences): ReversalManager {
        return ReversalManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTerminalConfigManager(sharedPreferences: SharedPreferences): TerminalConfigManager {
        return TerminalConfigManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providesLogonManager(sharedPreferences: SharedPreferences): LogonManager {
        return LogonManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providesTransactionManager(appDatabase: AppDatabase): TransactionManager {
        return TransactionManager(appDatabase)
    }
}