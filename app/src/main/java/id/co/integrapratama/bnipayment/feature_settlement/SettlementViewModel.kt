package id.co.integrapratama.bnipayment.feature_settlement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementRepository
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettlementViewModel @Inject constructor(
    private val settlementRepository: SettlementRepository
) : ViewModel() {
    companion object {
        private const val TAG = "SettlementViewModel"
    }

    private val _uiState = MutableStateFlow(SettlementUiState())
    val uiState: StateFlow<SettlementUiState> = _uiState.asStateFlow()

    fun onEvent(event: SettlementUiEvent) {
        when (event) {
            is SettlementUiEvent.LoadTotalSettlement -> {
                loadTotalSettlement()
            }
            is SettlementUiEvent.ShowPerformSettlementAndBatchUploadPromptDialog -> {
                _uiState.update {
                    it.copy(
                        showPerformSettlementAndBatchUploadPromptDialog = true
                    )
                }
            }
            is SettlementUiEvent.ApplyPerformSettlementAndBatchUploadPromptDialog -> {
                _uiState.update {
                    it.copy(
                        showPerformSettlementAndBatchUploadPromptDialog = false
                    )
                }
                performSettlementAndBatchUpload()
            }
        }
    }

    private fun loadTotalSettlement() {
        viewModelScope.launch {
            settlementRepository.getTotalSettlementSummary().collect { resource ->
                _uiState.update {
                    it.copy(
                        totalSettlementSummaryModelResult = resource
                    )
                }
            }
        }
    }

    private fun performSettlementAndBatchUpload() {
        viewModelScope.launch {
            settlementRepository.postSettlementAndBatchUpload().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                loadingMessage = resource.message ?: "Harap tunggu"
                            )
                        }
                    }

                    is Resource.Success -> {
                        printSettlementReceipt(
                            resource.data!!.settlementPrintBasedOnTemplateParameter
                        )
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadingMessage = "",
                            )
                        }
                    }
                }
            }
        }
    }

    private fun printSettlementReceipt(
        settlementPrintBasedOnTemplateParameter: PrintBasedOnTemplateParameter
    ) {
        viewModelScope.launch {
            settlementRepository.printSettlement(
                settlementPrintBasedOnTemplateParameter
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                loadingMessage = resource.message ?: "Harap tunggu"
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadingMessage = "",
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadingMessage = "",
                            )
                        }
                    }
                }
            }
        }
    }
}