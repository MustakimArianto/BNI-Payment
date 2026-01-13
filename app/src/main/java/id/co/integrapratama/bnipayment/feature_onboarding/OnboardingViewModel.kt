package id.co.integrapratama.bnipayment.feature_onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.bnipayment.BuildConfig
import id.co.integrapratama.sdk.core.AppManager
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appManager: AppManager
) : ViewModel() {

    fun isFirstInstall(): Boolean {
        return appManager.isFirstInstall()
    }

    fun updateFirstInstall() {
        appManager.updateFirstInstall()
    }

    fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }
}