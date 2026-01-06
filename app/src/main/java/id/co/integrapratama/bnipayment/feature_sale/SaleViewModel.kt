package id.co.integrapratama.bnipayment.feature_sale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.bnipayment.common.maskCardNumber
import id.co.integrapratama.sdk.feature_read_card.domain.ReadCardRepository
import id.co.integrapratama.sdk.feature_sale.domain.SaleRepository
import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import id.co.payment2go.terminalsdkhelper.core.util.Util
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    private val readCardRepository: ReadCardRepository,
) : ViewModel() {
    companion object {
        private const val TAG = "SaleViewModel"
    }
    private val _uiState = MutableStateFlow(SaleUiState())
    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    fun onEvent(event: SaleUiEvent) {
        when (event) {
            is SaleUiEvent.OnAmountChange -> {
                _uiState.value = _uiState.value.copy(amount = event.amount)
            }
        }
    }

    fun startCardReading() {
        injectAids()
    }

    private fun injectAids() {
        viewModelScope.launch {
            readCardRepository.injectAids().collectLatest { resourceAids ->
                when (resourceAids) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                statusMessage = resourceAids.message ?: "Harap tunggu"
                            )
                        }
                    }

                    is Resource.Success -> {
                        injectCapks()
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resourceAids.message ?: "Terjadi kesalahan",
                            )
                        }
                    }
                }
            }
        }
    }

    private fun injectCapks() {
        viewModelScope.launch {
            readCardRepository.injectCapks().collectLatest { resourceCapks ->
                when (resourceCapks) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                statusMessage = resourceCapks.message ?: "Harap tunggu"
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                statusMessage = ""
                            )
                        }
                        readCard()
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resourceCapks.message ?: "Terjadi kesalahan",
                            )
                        }
                    }
                }
            }
        }
    }

    private fun readCard() {
        viewModelScope.launch {
            val cardOption = CardOption(
                supportContactless = false,
                supportSwipe = false,
                supportDip = true
            )

            readCardRepository.readCard(
                cardOption = cardOption,
                amount = uiState.value.amount.toLong()
            )
                .collect { resourceReadCard ->
                    when (resourceReadCard) {
                        is Resource.Loading -> {
                            val loadingMessage: String = resourceReadCard.message.toString()
                            Log.d("loadingMessage", loadingMessage)
                            val lowerCaseLoadingMessage: String = loadingMessage.lowercase(Locale.getDefault())
                            fun parsingPresentingCardAgainMessage(): String {
                                var step = 1
                                val result = StringBuilder()
                                for (c in lowerCaseLoadingMessage) {
                                    if (step == 1) {
                                        if (c == ':') {
                                            step = 2
                                        }
                                    } else if (step == 2) {
                                        if (c != ' ') {
                                            step = 3
                                            result.append(c)
                                        }
                                    } else {
                                        result.append(c)
                                    }
                                }
                                return result.toString().replaceFirstChar { it.uppercaseChar() }
                            }

                            val parsingPresentingCardAgainMessageResult = parsingPresentingCardAgainMessage()
                            val cardReadOutput: CardReadOutput? = resourceReadCard.data?.cardReadOutput

                            _uiState.update {
                                it.copy(
                                    isLoading = true,
                                    isReadingCard = true,
                                    statusMessage = if (parsingPresentingCardAgainMessageResult.isNotBlank()) "" else loadingMessage
                                )
                            }


                            if (cardReadOutput?.cardNo?.isNotEmpty() == true) {
                                _uiState.update {
                                    it.copy(
                                        cardNumber = cardReadOutput.cardNo,
                                        maskedCardNumber = maskCardNumber(cardReadOutput.cardNo),
                                        isFinishedReadCard = true,
                                        statusMessage = if (parsingPresentingCardAgainMessageResult.isNotBlank()) "" else loadingMessage
                                    )
                                }
                            }
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isReadingCard = false,
                                    isFinishedReadCard = true,
                                    cardReadOutput = resourceReadCard.data?.cardReadOutput,
                                    cardNumber = resourceReadCard.data?.cardReadOutput?.cardNo
                                        ?: "",
                                )
                            }

                            viewModelScope.launch {
                                postSaleTransaction(
                                    isFromSaving = true,
                                    cardReadOutput = uiState.value.cardReadOutput
                                        ?: CardReadOutput()
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = resourceReadCard.message ?: "Terjadi kesalahan",
                                )
                            }
                        }
                    }
                }
        }
    }

    fun postSaleTransaction(isFromSaving: Boolean, cardReadOutput: CardReadOutput) {
        viewModelScope.launch {
            saleRepository.postSaleTransaction(isFromSaving, cardReadOutput)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true,
                                    statusMessage = resource.message ?: "Harap tunggu"
                                )
                            }
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isTransactionSuccess = true,
                                    statusMessage = resource.message ?: "Harap tunggu"
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

    fun printReceipt() {

    }
}