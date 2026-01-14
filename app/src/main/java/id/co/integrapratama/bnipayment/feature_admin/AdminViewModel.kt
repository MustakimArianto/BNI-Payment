package id.co.integrapratama.bnipayment.feature_admin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.sdk.core.TerminalConfigManager
import id.co.integrapratama.sdk.feature_aid_master.domain.AidMasterRepository
import id.co.integrapratama.sdk.feature_capk_master.domain.CapkMasterRepository
import id.co.integrapratama.sdk.feature_card_list.domain.CardListRepository
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val aidMasterRepository: AidMasterRepository,
    private val capkMasterRepository: CapkMasterRepository,
    private val cardListRepository: CardListRepository,
    private val terminalConfigManager: TerminalConfigManager, // Temporary, if logon repo created move this to the repo implementation
) : ViewModel() {

}