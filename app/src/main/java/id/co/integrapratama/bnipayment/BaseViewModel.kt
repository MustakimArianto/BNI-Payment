package id.co.integrapratama.bnipayment

import androidx.lifecycle.ViewModel
import id.co.integrapratama.sdk.core.TerminalConfigManager
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var terminalConfigManager: TerminalConfigManager


    fun getMerchantName(): String {
        return terminalConfigManager.getMerchantName() ?: ""
    }

    fun getTid(): String {
        return terminalConfigManager.getTid() ?: ""
    }

    fun getMid(): String {
        return terminalConfigManager.getMid() ?: ""
    }
}