package id.co.integrapratama.sdk.feature_void.data

import com.google.gson.JsonParser
import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.ReversalManager
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.exception.CustomMessageException
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.MTI
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.core.utils.toResourceError
import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.integrapratama.sdk.feature_sale.data.dto.ReversalCreditRequestDto
import id.co.integrapratama.sdk.feature_sale.data.dto.ReversalDebitRequestDto
import id.co.integrapratama.sdk.feature_void.domain.VoidRepository
import id.co.integrapratama.sdk.feature_void.domain.VoidRequestModel
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderValueComponent
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class VoidRepositoryImpl @Inject constructor(
    private val isoRepository: Iso8583Repository,
    private val appDatabase: AppDatabase,
    private val printRepository: PrintRepository,
    private val traceNumberManager: TraceNumberManager,
    private val terminalBatchManager: TerminalBatchManager,
    private val reversalManager: ReversalManager,
    private val stanManager: StanManager
) : VoidRepository {
    companion object {
        private const val TAG = "SaleRepositoryImpl"
        private const val SALE_DEBIT_PROCODE = "000000"
        private const val SALE_CREDIT_PROCODE = "020000"
    }

    override suspend fun postVoidTransaction(
        cardClassification: CardClassification,
        voidRequestModel: VoidRequestModel
    ): Flow<Resource<ByteArray>> {
        return flow {
            val cardNo = voidRequestModel.pan
            val processingCode = SALE_CREDIT_PROCODE
            val amount = voidRequestModel.amount.toString().padAmount()
            val transactionDateTime = Date()
            val stan = voidRequestModel.stan ?: ""
            val time = DateUtils.getTransactionTime(transactionDateTime)
            val date = DateUtils.getTransactionDate(transactionDateTime)
            val expiry = voidRequestModel.cardExpiry?.replace("/", "") ?: ""
            val posEntryMode = voidRequestModel.posEntryMode + "1"
            val panSeq = voidRequestModel.panSeq + "1"
            val nii = "041"
            val posConditionCode = "00"
            val track2 = "12345678901234567890"
            val rrn = "000051000004"
            val approvalCode = "030059"
            val responseCode = "00"
            val tid = "12345678"
            val mid = "123456789012345"
            val pinBlock = voidRequestModel.pinBlock
            val iccData = voidRequestModel.iccData
            val fld57 = "1".repeat(300)
            val transactionDetails = "2".repeat(300)
            val fld62 = "0006123456789012"
            val messageAuthCode = "0006123456789012"

            try {
                emit(Resource.Loading("Mengirim void"))
                traceNumberManager.saveLastTraceNo(traceNumberManager.getCurrentTraceNo())
                traceNumberManager.increment()

                reversalManager.saveVoidReversal(
                    if (cardClassification == CardClassification.DEBIT) {
                        ReversalDebitRequestDto(
                            pan = cardNo,
                            processingCode = processingCode,
                            amount = amount,
                            transactionDateTime = DateUtils.dateTimeFormat.format(date),
                            stan = stan,
                            time = time,
                            date = date,
                            expiry = expiry,
                            posEntryMode = posEntryMode,
                            nii = nii,
                            tid = tid,
                            mid = mid,
                        ).toString()
                    } else {
                        ReversalCreditRequestDto(
                            pan = cardNo,
                            processingCode = processingCode,
                            amount = amount,
                            stan = stan,
                            time = time,
                            date = date,
                            expiry = expiry,
                            posEntryMode = posEntryMode,
                            panSeq = panSeq,
                            nii = nii,
                            posConditionCode = posConditionCode,
                            approvalCode = approvalCode,
                            responseCode = responseCode,
                            tid = tid,
                            mid = mid,
                            iccData = iccData,
                            fld57 = fld57,
                            transactionDetails = transactionDetails,
                            fld62 = fld62,
                            messageAuthCode = messageAuthCode
                        ).toString()
                    }
                )

                val requestData = if (cardClassification == CardClassification.DEBIT) {
                    mutableMapOf<Int, String>().apply {
                        put(2, cardNo)
                        put(3, processingCode)
                        put(4, amount)
                        put(11, stan)
                        put(14, expiry)
                        put(22, posEntryMode)
                        put(24, nii)
                        put(25, posConditionCode)
                        put(35, track2)
                        put(37, rrn)
                        put(38, approvalCode)
                        put(41, tid)
                        put(42, mid)
                        put(52, pinBlock)
                        if (iccData.isNotEmpty()) {
                            put(55, iccData)
                        }
                        put(62, fld62)
                    }
                } else {
                    mutableMapOf<Int, String>().apply {
                        put(3, processingCode)
                        put(4, amount)
                        put(11, stan)
                        put(12, time)
                        put(13, date)
                        put(22, posEntryMode)
                        put(23, panSeq)
                        put(24, nii)
                        put(25, posConditionCode)
                        put(35, track2)
                        put(37, rrn)
                        put(38, approvalCode)
                        put(39, responseCode)
                        put(41, tid)
                        if (iccData.isNotEmpty()) {
                            put(55, iccData)
                        }
                        put(57, fld57)
                        put(61, transactionDetails)
                        put(62, fld62)
                        put(64, messageAuthCode)
                    }
                }

                val packedData = isoRepository.createRequest(
                    MTI.FINANCIAL.code,
                    requestData,
                )

                if (packedData.isEmpty()) {
                    throw Exception("Error saat membuat request void")
                }

                isoRepository.sendAndReceive(packedData).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            emit(Resource.Loading("Mengirim request void..."))
                        }

                        is Resource.Success -> {
                            emit(Resource.Success(response.data ?: byteArrayOf()))
                        }

                        is Resource.Error -> {
                            emit(Resource.Error(response.message ?: "Terjadi kesalahan"))
                        }
                    }
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, "postVoidTransaction: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }

    private suspend fun getVoidRequest(traceNo: String): VoidRequestModel {
        // Check the card transaction
        val voidRequest = appDatabase.cardTransactionDao().getTrxDataByTraceNo(traceNo)
        if (voidRequest == null) {
            throw CustomMessageException("Data tidak ditemukan untuk trace no: $traceNo")
        }
        return VoidRequestModel(
            invoice = voidRequest.invoice,
            invoiceDate = voidRequest.invoiceDate,
            issuerID = voidRequest.issuerID,
            issuerName = voidRequest.issuerName,
            saleType = voidRequest.saleType,
            batchNo = voidRequest.batchNo,
            authCode = voidRequest.authCode,
            amount = voidRequest.amount / 100L,
            payID = voidRequest.payID,
            pan = voidRequest.pan,
            mID = voidRequest.mID,
            tID = voidRequest.tID,
            printFormats = voidRequest.printFormats,
            refNo = voidRequest.refNo,
            txnTypeId = voidRequest.txnTypeId,
            programName = voidRequest.programName,
            cardExpiry = voidRequest.cardExpiry,
            cardAID = voidRequest.cardAID,
            cardAppName = voidRequest.cardAppName,
            customerName = voidRequest.customerName,
            currencyCode = voidRequest.currencyCode,
            tVRData = voidRequest.tVRData,
            tSIData = voidRequest.tSIData,
            txnCatCode = voidRequest.txnCatCode,
            txnCert = voidRequest.txnCert,
            stan = voidRequest.stan,
            maskedCardNo = voidRequest.maskedCardNo,
            insertModeCode = voidRequest.insertModeCode,
            rRNO = voidRequest.rrNo,
            txnStatus = voidRequest.txnStatus,
            cardType = voidRequest.cardType,
            cardTypeCode = voidRequest.cardTypeCode,
            acquiringBank = voidRequest.acquiringBank,
            secureData = voidRequest.secureData,
            posEntryMode = voidRequest.posEntryMode,
            tipAmount = voidRequest.tipAmount,
            cashAMT = voidRequest.cashAMT,
            feeAmount = voidRequest.feeAmount,
            refTxnTypeId = voidRequest.refTxnTypeId,
            tenure = voidRequest.tenure,
            bankTID = voidRequest.bankTID,
            bankMID = voidRequest.bankMID,
            eMIAmount = voidRequest.eMIAmount,
            panSeq = voidRequest.panSeq,
            iccData = voidRequest.iccData,
            responseCode = voidRequest.responseCode,
            mti = voidRequest.mti,
            jsonReceipt = voidRequest.jsonReceipt,
            templateJsonReceipt = voidRequest.templateJsonReceipt
        )
    }

    override suspend fun checkVoidTransaction(traceNo: String): Flow<Resource<VoidRequestModel>> {
        return flow {
            try {
                val paddedTraceNo = traceNo.padStart(6, '0')

                emit(Resource.Loading("Mengecek data void"))

                // Void request
                val voidRequestModel = getVoidRequest(paddedTraceNo)

                emit(Resource.Success(voidRequestModel))
            } catch (e: Exception) {
                LogSdk.error(TAG, "checkVoidTransaction: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }

    override suspend fun updateVoidTransaction(traceNo: String): Flow<Resource<Unit>> {
        return flow {
            try {
                val paddedTraceNo = traceNo.padStart(6, '0')

                emit(Resource.Loading("Mengecek data void"))

                // Void request
                val voidRequest = getVoidRequest(paddedTraceNo)

                val printBasedOnTemplateParameterBuilder = PrintBasedOnTemplateParameterBuilder().fromJson(
                    valueComponentJsonString = voidRequest.jsonReceipt,
                    templateComponentJsonString = voidRequest.templateJsonReceipt
                )

                // Edit VOID Label
                val voidValueComponentIndex = printBasedOnTemplateParameterBuilder.valueComponentList.indexOfFirst {
                    it.key == "Label"
                }
                var voidValueComponent = printBasedOnTemplateParameterBuilder.valueComponentList[voidValueComponentIndex]
                if (voidValueComponentIndex > 0) {
                    voidValueComponent = PrintBasedOnTemplateParameterBuilderValueComponent(
                        voidValueComponent.key,
                        "VOID"
                    )
                    printBasedOnTemplateParameterBuilder.editValueComponentBasedIndex(
                        index = voidValueComponentIndex,
                        valueComponent = voidValueComponent
                    )
                }

                // Edit Amount
                val amountValueComponentIndex = printBasedOnTemplateParameterBuilder.valueComponentList.indexOfFirst {
                    it.key == "Amount"
                }
                var amountValueComponent = printBasedOnTemplateParameterBuilder.valueComponentList[amountValueComponentIndex]
                if (amountValueComponentIndex > 0) {
                    amountValueComponent = PrintBasedOnTemplateParameterBuilderValueComponent(
                        amountValueComponent.key,
                        "- ${amountValueComponent.value}"
                    )
                    printBasedOnTemplateParameterBuilder.editValueComponentBasedIndex(
                        index = amountValueComponentIndex,
                        valueComponent = amountValueComponent
                    )
                }

                val printBasedOnTemplateParameter = printBasedOnTemplateParameterBuilder.toPrintBasedOnTemplateParameter {}

                appDatabase.cardTransactionDao().updateTransactionStatusToVoid(paddedTraceNo)
                appDatabase.cardTransactionDao().updateJsonReceiptAndTemplateJsonReceipt(
                    traceNo = paddedTraceNo,
                    jsonReceipt = printBasedOnTemplateParameter.jsonString,
                    templateJsonReceipt = printBasedOnTemplateParameter.printTemplateJsonString
                )

                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                LogSdk.error(TAG, "updateVoidTransaction: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }

    override suspend fun printVoidBasedTraceNo(traceNo: String): Flow<Resource<Unit>> {
        return flow {
            try {
                val paddedTraceNo = traceNo.padStart(6, '0')

                emit(Resource.Loading("Mengecek data void"))

                // Void request
                val voidRequest = getVoidRequest(paddedTraceNo)

                emit(Resource.Loading("Mencetak struk"))

                printRepository.printWithBuilder(
                    builder = PrintBasedOnTemplateParameterBuilder().fromJson(
                        valueComponentJsonString = voidRequest.jsonReceipt,
                        templateComponentJsonString = voidRequest.templateJsonReceipt
                    )
                ).collect()
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                LogSdk.error(TAG, "printVoidBasedTraceNo: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }

    override suspend fun postVoidReversal(): Flow<Resource<ByteArray>> {
        return flow {
            try {
                val reversalData = reversalManager.getVoidReversal()

                if (reversalData == null) {
                    emit(Resource.Error("Data reversal tidak ditemukan"))
                    return@flow
                }

                stanManager.increaseStan()
                traceNumberManager.increment()

                emit(Resource.Loading("Mengecek data reversal"))
                val processingCode = JsonParser.parseString(reversalData)
                    .asJsonObject
                    .get("processingCode")?.asString ?: ""

                val cardClassification = if (processingCode == SALE_DEBIT_PROCODE) {
                    CardClassification.DEBIT
                } else {
                    CardClassification.CREDIT
                }

                val request = if (cardClassification == CardClassification.DEBIT) {
                    mutableMapOf<Int, String>().apply {
                        val dto = ReversalDebitRequestDto.fromString(reversalData)
                        with(dto) {
                            put(2, pan)
                            put(3, processingCode)
                            put(4, amount)
                            put(7, transactionDateTime)
                            put(11, stan)
                            put(12, time)
                            put(13, date)
                            put(14, expiry)
                            put(22, posEntryMode)
                            put(24, nii)
                            put(41, tid)
                            put(42, mid)
                        }
                    }
                } else {
                    mutableMapOf<Int, String>().apply {
                        val dto = ReversalCreditRequestDto.fromString(reversalData)

                        with(dto) {
                            put(2, pan)
                            put(3, processingCode)
                            put(4, amount)
                            put(11, stan)
                            put(12, time)
                            put(13, date)
                            put(14, expiry)
                            put(22, posEntryMode)
                            put(23, panSeq)
                            put(24, nii)
                            put(25, posConditionCode)
                            put(38, approvalCode)
                            put(39, responseCode)
                            put(41, tid)
                            put(42, mid)
                            put(55, iccData)
                            put(57, fld57)
                            put(61, transactionDetails)
                            put(62, fld62)
                            put(64, messageAuthCode)
                        }
                    }
                }

                val packedData = isoRepository.createRequest(
                    MTI.REVERSAL.code, request
                )

                if (packedData.isEmpty()) {
                    emit(Resource.Error("Error saat membuat request reversal"))
                    return@flow
                }

                isoRepository.sendAndReceive(packedData).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            emit(Resource.Loading("Mengirim data reversal..."))
                        }

                        is Resource.Success -> {
                            emit(Resource.Success(resource.data ?: byteArrayOf()))
                        }

                        is Resource.Error -> {
                            emit(Resource.Error(resource.message ?: "Terjadi kesalahan"))
                        }
                    }
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, "postVoidReversal: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }
}