package id.co.integrapratama.bnipayment.feature_admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.sdk.feature_aid_master.domain.AidMasterRepository
import id.co.integrapratama.sdk.feature_capk_master.domain.CapkMasterRepository
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val aidMasterRepository: AidMasterRepository,
    private val capkMasterRepository: CapkMasterRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState = _uiState.asStateFlow()

    fun performLogon() {
        getAidMaster()
    }

    fun getAidMaster() {
        viewModelScope.launch {
            aidMasterRepository.getAidMaster().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true, loadingMessage = "Mengambil data Aid Master"
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                resultMessage = "Berhasil mengambil data Aid Master"
                            )
                        }

                        getCapkMaster()
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isError = true,
                                resultMessage = resource.message ?: "Terjadi kesalahan"
                            )
                        }
                    }
                }
            }
        }
    }

    fun getCapkMaster() {
        viewModelScope.launch {
            capkMasterRepository.getCapkMaster().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true, loadingMessage = "Mengambil data Capk Master"
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                resultMessage = "Berhasil mengambil data Capk Master"
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isError = true,
                                resultMessage = resource.message ?: "Terjadi kesalahan"
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearUiState() {
        _uiState.value = AdminUiState()
    }
}