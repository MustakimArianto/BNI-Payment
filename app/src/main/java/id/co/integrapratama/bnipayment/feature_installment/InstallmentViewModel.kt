package id.co.integrapratama.bnipayment.feature_installment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.integrapratama.bnipayment.common.maskCardNumber
import id.co.integrapratama.iso8583sdk.IsoMessage
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.iso8583.IsoConfig
import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds
import id.co.integrapratama.sdk.core.utils.CardUtil
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.StringUtil
import id.co.integrapratama.sdk.feature_bin_range.domain.BinRangeRepository
import id.co.integrapratama.sdk.feature_bin_range.domain.BinType
import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.integrapratama.sdk.feature_installment.core.InstallmentPrintTemplateFactory
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentRepository
import id.co.integrapratama.sdk.feature_read_card.domain.ReadCardRepository
import id.co.integrapratama.sdk.feature_sale.domain.TransactionRecord
import id.co.payment2go.terminalsdkhelper.common.DecideCVMStatusResult
import id.co.payment2go.terminalsdkhelper.common.device_type_value.isPhysicalKeypadSupported
import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.common.pinpad.OnPinPadResult
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import id.co.payment2go.terminalsdkhelper.core.DeviceTypeManager
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
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InstallmentViewModel @Inject constructor(
    private val deviceTypeManager: DeviceTypeManager,
    private val deviceManagerUtility: DeviceManagerUtility,
    private val traceNumberManager: TraceNumberManager,
    private val terminalBatchManager: TerminalBatchManager,
    private val stanManager: StanManager,
    private val installmentRepository: InstallmentRepository,
    private val readCardRepository: ReadCardRepository,
    private val binRangeRepository: BinRangeRepository
) : ViewModel() {
    companion object {
        private const val TAG = "InstallmentViewModel"
    }

    private val _uiState = MutableStateFlow(InstallmentUiState())
    val uiState: StateFlow<InstallmentUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    fun onEvent(event: InstallmentUiEvent) {
        when (event) {
            is InstallmentUiEvent.OnAmountChange -> {
                _uiState.value = _uiState.value.copy(amount = event.amount)
            }

            is InstallmentUiEvent.OnConfirmCard -> {
                confirmCard()
            }

            is InstallmentUiEvent.MappingPinpad -> {
                val deviceTypeValue = deviceTypeManager.getDeviceTypeValue()

                if (deviceTypeValue.isPhysicalKeypadSupported) {
                    physicalPinpad()
                } else {
                    screenPinpad(event.containerInfo, event.pinpadMap)
                }
            }

            is InstallmentUiEvent.MappingOfflinePinpad -> {
                val deviceTypeValue = deviceTypeManager.getDeviceTypeValue()

                if (deviceTypeValue.isPhysicalKeypadSupported) {
                    physicalOfflinePinpad()
                } else {
                    screenOfflinePinpad(event.containerInfo, event.pinpadMap)
                }
            }

            is InstallmentUiEvent.OnInstallmentPlanSelectionChange -> {
                _uiState.update {
                    it.copy(
                        selectedInstallmentPlan = event.installmentPlanModel
                    )
                }
            }

            is InstallmentUiEvent.OnInstallmentPeriodSelectionChange -> {
                _uiState.update {
                    it.copy(
                        selectedInstallmentPeriod = event.installmentPeriodModel
                    )
                }
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

            val cardOption = CardOption(
                supportContactless = true,
                supportSwipe = false,
                supportDip = true
            )

            readCardRepository.readCard(
                amount = uiState.value.amount.toLong(),
                cardOption = cardOption,
            ).collect { resourceReadCard ->
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

    fun getInstallmentPeriodAndPlanList() {
        viewModelScope.launch {
            installmentRepository.getInstallmentPeriodList().collect { resource ->
                _uiState.update {
                    it.copy(
                        installmentPeriodListResource = resource
                    )
                }
            }
            installmentRepository.getInstallmentPlanList().collect { resource ->
                _uiState.update {
                    it.copy(
                        installmentPlanListResource = resource
                    )
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

                        val cardClassification =
                            binRangeRepository.classifyCard(resource.data ?: BinType.UNKNOWN)

                        postInstallmentTransaction(
                            cardClassification,
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

    fun postInstallmentTransaction(
        cardClassification: CardClassification,
        cardReadOutput: CardReadOutput
    ) {
        _uiState.update {
            it.copy(
                transactionDateTime = DateUtils.getCurrentTransactionDateTime()
            )
        }

        viewModelScope.launch {
            installmentRepository.postInstallmentTransaction(
                cardClassification,
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
                            headerLength = 2
                        )

                        val responseCode = response.getField(39)

                        if (responseCode == "00") {
                            val emvData = response.getField(55)
                            val authCode = response.getField(38)

                            verifyEmvHost(
                                emvHost = emvData,
                                authCode = authCode,
                                arc = responseCode,
                                authorizeFlag = "00",
                                cardReadOutput = cardReadOutput
                            )
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
            readCardRepository.confirmCard(uiState.value.amount.toLong()).collect { resource ->
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
                                loadingMessage = "Confirm Card..."
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
        authorizeFlag: String?,
        cardReadOutput: CardReadOutput
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

                        saveTransactionToDatabase(cardReadOutput)
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadingMessage = "",
                                isTransactionFinished = true,
                            )
                        }

                        saveTransactionToDatabase(cardReadOutput)
//                        _uiState.update {
//                            it.copy(
//                                isLoading = false,
//                                isTransactionFinished = true,
//                                transactionResultMessage = resource.message ?: "Terjadi kesalahan",
//                            )
//                        }
                    }
                }
            }
        }
    }

    private fun saveTransactionToDatabase(cardReadOutput: CardReadOutput) {
        viewModelScope.launch {
            val currentDate = Date()
            val currentTraceNoText = traceNumberManager.getCurrentTraceNo().toString().padStart(6, '0')
            val currentBatchNoText = terminalBatchManager.getCurrentBatch().toString().padStart(6, '0')
            val currentStanText = stanManager.getCurrentStan().toString().padStart(6, '0')
            val amountText = StringUtil.formatRupiahCurrency(
                ((cardReadOutput.txnAmount.toLongOrNull() ?: 0) / 100L).toString()
            )
            val authCode = "711162"
            val refNo = "000047111620000"
            val installmentPrintTemplateFactory = InstallmentPrintTemplateFactory(
                branchName = "DUMMY TRX",
                branchAddress = "JL. JENDRAL SUDIRMAN",
                branchCity = "JAKARTA",
                terminalId = "1234567890",
                merchantId = "1234567890",
                cardType = cardReadOutput.cardAppName,
                exp = cardReadOutput.cardExpiry,
                cardNumber = cardReadOutput.cardNo,
                cardMethod = CardUtil.getCardMethodFromPosEntryMode(cardReadOutput.posEntryMode),
                date = DateUtils.getReceiptTransactionDate(currentDate),
                time = DateUtils.getReceiptTransactionTime(currentDate),
                batch = currentBatchNoText,
                trace = currentTraceNoText,
                ref = refNo,
                appr = authCode,
                amount = amountText,
                version = "V2019.1.0.0.8",
                serialNumber = deviceManagerUtility.getSerialNumberDevice(),
            )
            val installmentPrintBasedOnTemplateParameter = installmentPrintTemplateFactory.getPrintBasedOnTemplateParameter {}
            installmentRepository.insertCardTransactionToDatabase(
                TransactionRecord(
                    invoice = currentTraceNoText,
                    invoiceDate = DateUtils.getFullTransactionDateTime(currentDate),
                    issuerID = "3",
                    issuerName = "INSTALLMENT",
                    saleType = "INSTALLMENT",
                    batchNo = currentTraceNoText,
                    authCode = authCode,
                    amount = (cardReadOutput.txnAmount.toLongOrNull() ?: 0),
                    payID = "",
                    pan = cardReadOutput.cardNo,
                    mID = "1234567890",
                    tID = "1234567890",
                    printFormats = "",
                    refNo = refNo,
                    txnTypeId = "",
                    programName = "",
                    cardExpiry = cardReadOutput.cardExpiry,
                    cardAID = cardReadOutput.cardAID,
                    cardAppName = cardReadOutput.cardAppName,
                    customerName = cardReadOutput.customerName,
                    currencyCode = cardReadOutput.currencyCode,
                    tVRData = cardReadOutput.terminalVerificationResults,
                    tSIData = cardReadOutput.TSIData,
                    txnCatCode = cardReadOutput.txnCategoryCode,
                    txnCert = cardReadOutput.transactionCertificate,
                    stan = currentStanText,
                    maskedCardNo = CardUtil.panMasking(cardReadOutput.cardNo, cardReadOutput.cardNo),
                    insertModeCode = cardReadOutput.insertModeCode,
                    rrNo = refNo,
                    txnStatus = "Success",
                    cardType = "",
                    cardTypeCode = "",
                    acquiringBank = "",
                    secureData = "",
                    posEntryMode = cardReadOutput.posEntryMode,
                    tipAmount = 0,
                    cashAMT = 0,
                    feeAmount = 0,
                    refTxnTypeId = "",
                    tenure = "",
                    bankTID = "",
                    bankMID = "",
                    cashierID = "",
                    terminalCapability = cardReadOutput.terminalCapability,
                    jsonReq = "",
                    jsonResp = "",
                    eMIAmount = 0L,
                    redeemAmount = 0L,
                    pinBlock = cardReadOutput.pinBlock,
                    isTxnActive = false,
                    isTxnVoid = false,
                    isVoidApplicable = false,
                    isLoyaltyVoid = false,
                    loyaltyData = "",
                    panSeq = cardReadOutput.PANSEQ,
                    iccData = cardReadOutput.emvData,
                    responseCode = "",
                    mti = "",
                    jsonReceipt = installmentPrintBasedOnTemplateParameter.jsonString,
                    templateJsonReceipt = installmentPrintBasedOnTemplateParameter.printTemplateJsonString
                )
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                loadingMessage = it.loadingMessage
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
                        traceNumberManager.increment()
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

    fun setErrorMessage(message: String) {
        _uiState.update {
            it.copy(errorMessage = message)
        }
    }

    fun clearErrorMessage() {
        _uiState.update {
            it.copy(errorMessage = "")
        }
    }

    fun clearUiState() {
        _uiState.update {
            InstallmentUiState()
        }
    }

    fun printReceipt() {
        viewModelScope.launch {
            installmentRepository.printReceiptBasedLastTraceNo().collect { resource ->
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
                                isTransactionFinished = true,
                                errorMessage = resource.message ?: "Terjadi kesalahan",
                            )
                        }
                    }
                }
            }
        }
    }
}