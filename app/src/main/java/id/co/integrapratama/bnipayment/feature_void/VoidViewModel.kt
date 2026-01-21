package id.co.integrapratama.bnipayment.feature_void

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.bnipayment.BaseViewModel
import id.co.integrapratama.sdk.core.ReversalManager
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.feature_bin_range.domain.BinRangeRepository
import id.co.integrapratama.sdk.feature_read_card.domain.ReadCardRepository
import id.co.integrapratama.sdk.feature_void.domain.VoidRepository
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoidViewModel @Inject constructor(
    private val deviceTypeManager: DeviceTypeManager,
    private val voidRepository: VoidRepository,
    private val readCardRepository: ReadCardRepository,
    private val binRangeRepository: BinRangeRepository,
    private val traceNumberManager: TraceNumberManager,
    private val stanManager: StanManager,
    private val batchManager: TerminalBatchManager,
    private val reversalManager: ReversalManager,
) : BaseViewModel() {
    companion object {
        private const val TAG = "VoidViewModel"
    }
    private val _uiState = MutableStateFlow(VoidUiState())
    val uiState: StateFlow<VoidUiState> = _uiState.asStateFlow()

    init {
        getListTransaction()
    }

    fun onEvent(event: VoidUiEvent) {
        when (event) {
            is VoidUiEvent.OnTraceNoChange -> {
                _uiState.update {
                    it.copy(
                        traceNo = event.traceNo
                    )
                }
            }

            is VoidUiEvent.ConfirmVoid -> {
                confirmVoid()
            }

            is VoidUiEvent.TransactionListClicked -> {
                _uiState.update {
                    it.copy(
                        currentVoidTransaction = event.transaction
                    )
                }

                confirmVoid()
            }

            is VoidUiEvent.SubmitVoid -> {
                submitVoid()
            }

            is VoidUiEvent.PrintReceipt -> {
                printReceiptBasedTraceNo()
            }

            is VoidUiEvent.SetErrorMessage -> {
                _uiState.update {
                    it.copy(
                        errorMessage = event.errorMessage
                    )
                }
            }
        }
    }

    fun getListTransaction() {
        viewModelScope.launch {
            voidRepository.getListTransaction().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                transactionList = resource.data
                            )
                        }
                    }

                    is Resource.Error -> {

                    }
                }
            }
        }


    }
    private fun confirmVoid() {
        viewModelScope.launch {
            voidRepository.checkVoidTransaction(_uiState.value.traceNo).collect { resource ->
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
                        val voidRequestModel = resource.data!!
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "",
                                loadingMessage = "",
                                voidRequestModel = voidRequestModel,
                                hasConfirmVoid = true
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadingMessage = "",
                                errorMessage = resource.message ?: "Terjadi kesalahan",
                            )
                        }
                    }
                }
            }
        }
    }

    private fun submitVoid() {
        viewModelScope.launch {
            val voidRequestModel = _uiState.value.voidRequestModel ?: return@launch
            voidRepository.postVoidTransaction(voidRequestModel).collect { resource ->
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
                        updateTransactionStatusToVoid()
                        saveVoidToDatabase()
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadingMessage = "",
                                errorMessage = resource.message ?: "Terjadi kesalahan",
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveVoidToDatabase() {
        viewModelScope.launch {
            voidRepository.createVoidTransaction(_uiState.value.traceNo).collect { resource ->
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
                                errorMessage = "",
                                loadingMessage = "",
                                isTransactionFinished = true,
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadingMessage = "",
                                errorMessage = resource.message ?: "Terjadi kesalahan",
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateTransactionStatusToVoid() {
        viewModelScope.launch {
            voidRepository.updateTransactionStatusToVoid(_uiState.value.traceNo).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "",
                            )
                        }
                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    private fun printReceiptBasedTraceNo() {
        viewModelScope.launch {
            voidRepository.printVoidBasedTraceNo(_uiState.value.traceNo).collect { resource ->
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
                                isTransactionFinished = true,
                                errorMessage = "",
                                loadingMessage = resource.message ?: "Harap tunggu"
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message ?: "Terjadi kesalahan",
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update {
            it.copy(errorMessage = "")
        }
    }

    fun clearUiState() {
        _uiState.update {
            VoidUiState()
        }
    }
}