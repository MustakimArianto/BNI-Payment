package id.co.integrapratama.bnipayment.feature_mini_atm.balance_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.bnipayment.common.maskCardNumber
import id.co.integrapratama.iso8583sdk.IsoMessage
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.iso8583.IsoConfig
import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds
import id.co.integrapratama.sdk.feature_bin_range.domain.BinRangeRepository
import id.co.integrapratama.sdk.feature_bin_range.domain.BinType
import id.co.integrapratama.sdk.feature_mini_atm.balance_info.domain.MiniATMBalanceInfoRepository
import id.co.integrapratama.sdk.feature_mini_atm.balance_info.domain.parseBalanceField
import id.co.integrapratama.sdk.feature_read_card.domain.ReadCardRepository
import id.co.payment2go.terminalsdkhelper.common.DecideCVMStatusResult
import id.co.payment2go.terminalsdkhelper.common.device_type_value.isPhysicalKeypadSupported
import id.co.payment2go.terminalsdkhelper.common.pinpad.OnPinPadResult
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MiniATMBalanceInfoViewModel @Inject constructor(
    private val readCardRepository: ReadCardRepository,
    private val miniATMBalanceInfoRepository: MiniATMBalanceInfoRepository,
    private val binRangeRepository: BinRangeRepository,
    private val deviceTypeManager: DeviceTypeManager,
    private val stanManager: StanManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MiniATMBalanceInfoUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    fun startCardReading() {
        injectAids()
    }

    fun onEvent(event: MiniATMBalanceInfoUiEvent) {
        when (event) {
            is MiniATMBalanceInfoUiEvent.SetContactless -> {
                _uiState.update {
                    it.copy(
                        isContactless = event.isContactless
                    )
                }
            }

            MiniATMBalanceInfoUiEvent.OnConfirmCard -> {
                confirmCard()
            }

            is MiniATMBalanceInfoUiEvent.MappingPinpad -> {
                val deviceTypeValue = deviceTypeManager.getDeviceTypeValue()

                if (deviceTypeValue.isPhysicalKeypadSupported) {
                    physicalPinpad()
                } else {
                    screenPinpad(event.containerInfo, event.pinpadMap)
                }
            }

            is MiniATMBalanceInfoUiEvent.MappingOfflinePinpad -> {
                val deviceTypeValue = deviceTypeManager.getDeviceTypeValue()

                if (deviceTypeValue.isPhysicalKeypadSupported) {
                    physicalOfflinePinpad()
                } else {
                    screenOfflinePinpad(event.containerInfo, event.pinpadMap)
                }
            }
        }
    }

    private fun injectAids() {
        viewModelScope.launch {
            readCardRepository.injectAids().collectLatest { resourceAids ->
                when (resourceAids) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                loadingMessage = resourceAids.message ?: "Harap tunggu"
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
                                loadingMessage = resourceCapks.message ?: "Harap tunggu"
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadingMessage = ""
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
            stanManager.increaseStan()

            readCardRepository.readCard(
                amount = 0L,
                cardOption = uiState.value.cardOption,
            ).collect { resourceReadCard ->
                when (resourceReadCard) {
                    is Resource.Loading -> {
                        val loadingMessage: String = resourceReadCard.message.toString()
                        Log.d("loadingMessage", loadingMessage)
                        val lowerCaseLoadingMessage: String =
                            loadingMessage.lowercase(Locale.getDefault())

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

                        val parsingPresentingCardAgainMessageResult =
                            parsingPresentingCardAgainMessage()
                        val cardReadOutput: CardReadOutput? = resourceReadCard.data?.cardReadOutput

                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                isReadingCard = true,
                                loadingMessage = if (parsingPresentingCardAgainMessageResult.isNotBlank()) "" else loadingMessage
                            )
                        }


                        if (cardReadOutput?.cardNo?.isNotEmpty() == true) {
                            _uiState.update {
                                it.copy(
                                    cardNumber = cardReadOutput.cardNo,
                                    maskedCardNumber = maskCardNumber(cardReadOutput.cardNo),
                                    isFinishedReadCard = true,
                                    loadingMessage = if (parsingPresentingCardAgainMessageResult.isNotBlank()) "" else loadingMessage
                                )
                            }
                        }


                        if (resourceReadCard.data != null) {
                            if (resourceReadCard.data!!.isShowPinpad) {
                                _uiState.update {
                                    it.copy(
                                        isShowPinpad = true,
                                        isPhysicalKeyboard =
                                            deviceTypeManager.getDeviceTypeValue().isPhysicalKeypadSupported,
                                        onInsertOnlinePinAction =
                                            resourceReadCard.data!!.onInsertOnlinePinAction,
                                    )
                                }
                            } else if (resourceReadCard.data!!.isShowOfflinePinpad) {
                                _uiState.update {
                                    it.copy(
                                        isShowOfflinePinpad = true,
                                        isPhysicalKeyboard =
                                            deviceTypeManager.getDeviceTypeValue().isPhysicalKeypadSupported,
                                        onInsertOfflinePinAction =
                                            resourceReadCard.data!!.onInsertOfflinePinAction,
                                    )
                                }
                            }

                            if (parsingPresentingCardAgainMessageResult.isNotBlank()) {
                                _uiState.update {
                                    it.copy(
                                        presentCardAgainMessage = parsingPresentingCardAgainMessageResult
                                    )
                                }
                            }
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isReadingCard = false,
                                cardReadOutput = resourceReadCard.data?.cardReadOutput,
                                cardNumber = resourceReadCard.data?.cardReadOutput?.cardNo
                                    ?: "",
                            )
                        }

                        checkBinRange(resourceReadCard.data?.cardReadOutput?.cardNo ?: "")
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

    fun checkBinRange(cardNumber: String) {
        viewModelScope.launch {
            binRangeRepository.getBinType(cardNumber).collect { resource ->
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
                                binType = resource.data ?: BinType.UNKNOWN
                            )
                        }

                        binRangeRepository.classifyCard(resource.data ?: BinType.UNKNOWN)

                        postBalanceInfo(
                            cardReadOutput = uiState.value.cardReadOutput
                                ?: CardReadOutput()
                        )
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

    fun postBalanceInfo(
        cardReadOutput: CardReadOutput
    ) {
        _uiState.update {
            it.copy(
                transactionDateTime = Date()
            )
        }

        viewModelScope.launch {
            miniATMBalanceInfoRepository.postBalanceInfoTransaction(
                isFromSaving = true,
                cardReadOutput,
                uiState.value.transactionDateTime
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
                        val response = IsoMessage().unpack(
                            data = resource.data ?: byteArrayOf(),
                            specs = IsoConfig.genericSpec,
                        )

                        val responseCode = response.getField(39)

                        if (responseCode == "00") {
                            val balanceField = parseBalanceField(response.getField(44) ?: "")
                            val responseCode = response.getField(39)
                            val authCode = response.getField(38)
                            val emvData = response.getField(55)

                            _uiState.update {
                                it.copy(
                                    balance = balanceField.availableBalance.toString(),
                                )
                            }

                            if (uiState.value.isContactless) {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        loadingMessage = "",
                                        isTransactionFinished = true,
                                    )
                                }
                            } else {
                                verifyEmvHost(
                                    emvHost = emvData,
                                    authCode = authCode,
                                    arc = responseCode,
                                    authorizeFlag = "00"
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = resource.message ?: "Terjadi kesalahan",
                                )
                            }
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

    fun confirmCard() {
        viewModelScope.launch {
            readCardRepository.confirmCard(0L).collect { resource ->
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
                                isCardConfirmed = true
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

    fun physicalPinpad() {
        viewModelScope.launch {
            readCardRepository.physicalPinpad(
                cardNumber = uiState.value.cardNumber,
                onInsertOnlinePinAction = null
            ).collect { pinpadEvent ->
                val result = pinpadEvent.result
                val action = pinpadEvent.action

                when (result) {
                    is OnPinPadResult.OnInput -> {
                        val maskedText = "*".repeat(result.p1)
                        _uiState.update {
                            it.copy(
                                pin = maskedText
                            )
                        }
                    }

                    is OnPinPadResult.OnError -> {
                        _uiState.update {
                            it.copy(isShowPinpad = false)
                        }
                        action?.decideCVMStatus(
                            DecideCVMStatusResult.FAIL
                        )
                    }

                    is OnPinPadResult.OnConfirm -> {
                        _uiState.update {
                            it.copy(
                                isShowPinpad = false,
                                loadingMessage = "Confirm Card..."
                            )
                        }
                        val pinBlock = String(result.data ?: byteArrayOf())
                        confirmInputPin(
                            pinBlock,
                            result.isNonPin
                        )
                    }

                    is OnPinPadResult.OnCancel -> {
                        _uiState.update {
                            it.copy(isShowPinpad = false)
                        }

                        action?.decideCVMStatus(
                            DecideCVMStatusResult.CANCEL
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    fun physicalOfflinePinpad() {
        viewModelScope.launch {
            readCardRepository.physicalOfflinePinpad(
                cardNumber = uiState.value.cardNumber,
                onInsertOfflinePinAction = null
            ).collect { pinpadEvent ->
                val result = pinpadEvent.result
                val action = pinpadEvent.action

                when (result) {
                    is OnPinPadResult.OnInput -> {
                        val maskedText = "*".repeat(result.p1)
                        _uiState.update {
                            it.copy(
                                pin = maskedText
                            )
                        }
                    }

                    is OnPinPadResult.OnError -> {
                        _uiState.update {
                            it.copy(isShowOfflinePinpad = false)
                        }
                        action?.decideCVMStatus(
                            DecideCVMStatusResult.FAIL
                        )
                    }

                    is OnPinPadResult.OnConfirm -> {
                        _uiState.update {
                            it.copy(
                                isShowOfflinePinpad = false,
                                loadingMessage = "Konfirmasi kartu..."
                            )
                        }
                        val pinBlock = String(result.data ?: byteArrayOf())
                        confirmInputOfflinePin(
                            pinBlock,
                            result.isNonPin
                        )
                    }

                    is OnPinPadResult.OnCancel -> {
                        _uiState.update {
                            it.copy(isShowOfflinePinpad = false)
                        }

                        action?.decideCVMStatus(
                            DecideCVMStatusResult.CANCEL
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    private fun screenPinpad(
        containerInfo: CustomPinpadUiBounds,
        pinpadMap: List<CustomPinpadUiBounds>
    ) {
        viewModelScope.launch {
            readCardRepository.screenPinpad(
                cardNumber = uiState.value.cardNumber,
                containerInfo = containerInfo,
                pinpadMap = pinpadMap,
                onInsertOnlinePinAction = uiState.value.onInsertOnlinePinAction
            ).collect { pinpadEvent ->
                val result = pinpadEvent.result
                val action = pinpadEvent.action

                when (result) {
                    OnPinPadResult.OnCancel -> {
                        _uiState.update {
                            it.copy(isShowPinpad = false)
                        }
                        action?.decideCVMStatus(
                            DecideCVMStatusResult.CANCEL
                        )
                    }

                    is OnPinPadResult.OnConfirm -> {
                        _uiState.update {
                            it.copy(
                                isShowPinpad = false,
                                isLoading = true,
                                loadingMessage = "Konfirmasi kartu"
                            )
                        }
                        val pinBlock = String(result.data ?: byteArrayOf())
                        confirmInputPin(pinBlock, result.isNonPin)
                    }

                    is OnPinPadResult.OnError -> {
                        _uiState.update {
                            it.copy(isShowPinpad = false)
                        }
                        action?.decideCVMStatus(
                            DecideCVMStatusResult.FAIL
                        )
                    }

                    is OnPinPadResult.OnInput -> {
                        val maskedText = "*".repeat(result.p1)
                        _uiState.update {
                            it.copy(
                                pin = maskedText
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun screenOfflinePinpad(
        containerInfo: CustomPinpadUiBounds,
        pinpadMap: List<CustomPinpadUiBounds>
    ) {
        viewModelScope.launch {
            readCardRepository.screenOfflinePinpad(
                cardNumber = uiState.value.cardNumber,
                containerInfo = containerInfo,
                pinpadMap = pinpadMap,
                onInsertOfflinePinAction = uiState.value.onInsertOfflinePinAction
            ).collect { pinpadEvent ->
                val result = pinpadEvent.result
                val action = pinpadEvent.action

                when (result) {
                    OnPinPadResult.OnCancel -> {
                        _uiState.update {
                            it.copy(isShowOfflinePinpad = false)
                        }
                        action?.decideCVMStatus(
                            DecideCVMStatusResult.CANCEL
                        )
                    }

                    is OnPinPadResult.OnConfirm -> {
                        _uiState.update {
                            it.copy(
                                isShowOfflinePinpad = false,
                                isLoading = true,
                                loadingMessage = "Konfirmasi kartu"
                            )
                        }
                        val pinBlock = String(result.data ?: byteArrayOf())
                        confirmInputOfflinePin(pinBlock, result.isNonPin)
                    }

                    is OnPinPadResult.OnError -> {
                        _uiState.update {
                            it.copy(isShowOfflinePinpad = false)
                        }
                        action?.decideCVMStatus(
                            DecideCVMStatusResult.FAIL
                        )
                    }

                    is OnPinPadResult.OnInput -> {
                        val maskedText = "*".repeat(result.p1)
                        _uiState.update {
                            it.copy(
                                pin = maskedText
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    fun confirmInputPin(pinBlock: String, nonPin: Boolean) {
        viewModelScope.launch {
            readCardRepository.confirmInputPin(pinBlock, nonPin)
        }
    }

    fun confirmInputOfflinePin(pinBlock: String, nonPin: Boolean) {
        viewModelScope.launch {
            readCardRepository.confirmInputPin(pinBlock, nonPin)
        }
    }

    fun verifyEmvHost(
        emvHost: String?,
        authCode: String?,
        arc: String?,
        authorizeFlag: String?
    ) {
        viewModelScope.launch {
            readCardRepository.verifyEMVHost(
                emvHost, authCode, arc, authorizeFlag
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
                                isTransactionFinished = true,
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isTransactionFinished = true,
                                transactionResultMessage = resource.message ?: "Terjadi kesalahan",
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getPrintTemplate(): PrintBasedOnTemplateParameter {
        return miniATMBalanceInfoRepository.preparePrintingData(
            uiState.value.balance,
            uiState.value.cardReadOutput ?: CardReadOutput(),
            uiState.value.transactionDateTime
        )
    }

    fun printReceipt() {
        viewModelScope.launch {
            miniATMBalanceInfoRepository.printCurrentReceipt(getPrintTemplate())
                .collect { resource ->
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
}