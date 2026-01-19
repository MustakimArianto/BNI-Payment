package id.co.integrapratama.bnipayment.feature_settlement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementRepository
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettlementViewModel @Inject constructor(
    private val deviceTypeManager: DeviceTypeManager,
    private val deviceManagerUtility: DeviceManagerUtility,
    private val traceNumberManager: TraceNumberManager,
    private val terminalBatchManager: TerminalBatchManager,
    private val stanManager: StanManager,
    private val settlementRepository: SettlementRepository
) : ViewModel() {
    companion object {
        private const val TAG = "SettlementViewModel"
    }

    private val _uiState = MutableStateFlow(SettlementUiState())
    val uiState: StateFlow<SettlementUiState> = _uiState.asStateFlow()

    fun onEvent(event: SettlementUiEvent) {
        when (event) {
            is SettlementUiEvent.LoadTotalSettlement -> {}
            is SettlementUiEvent.ShowSettlementConfirmationDialog -> {}
            is SettlementUiEvent.PerformSettlementAndBatchUpload -> {}
        }
    }
}