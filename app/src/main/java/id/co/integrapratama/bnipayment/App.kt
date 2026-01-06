package id.co.integrapratama.bnipayment

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import id.co.payment2go.terminalsdkhelper.common.BindService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var bindService: BindService

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            try {
                bindService.bindServiceSDK()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}