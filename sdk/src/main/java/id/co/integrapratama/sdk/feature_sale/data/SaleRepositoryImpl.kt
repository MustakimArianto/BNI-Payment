package id.co.integrapratama.sdk.feature_sale.data

import android.util.Log
import com.google.gson.JsonParser
import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.ReversalManager
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.model.MTI
import id.co.integrapratama.sdk.core.utils.CardUtil
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.StringUtil
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.core.utils.toResourceError
import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.integrapratama.sdk.feature_sale.core.SalePrintTemplateFactory
import id.co.integrapratama.sdk.feature_sale.data.dto.ReversalCreditRequestDto
import id.co.integrapratama.sdk.feature_sale.data.dto.ReversalDebitRequestDto
import id.co.integrapratama.sdk.feature_sale.data.local.CardTransactionEntity
import id.co.integrapratama.sdk.feature_sale.domain.SaleRepository
import id.co.integrapratama.sdk.feature_sale.domain.TransactionRecord
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val isoRepository: Iso8583Repository,
    private val appDatabase: AppDatabase,
    private val printRepository: PrintRepository,
    private val traceNumberManager: TraceNumberManager,
    private val terminalBatchManager: TerminalBatchManager,
    private val reversalManager: ReversalManager,
    private val stanManager: StanManager
) : SaleRepository {
    companion object {
        private const val TAG = "SaleRepositoryImpl"
        private const val SALE_DEBIT_PROCODE = "000000"
        private const val SALE_CREDIT_PROCODE = "020000"
    }

    override suspend fun postSaleTransaction(
        cardClassification: CardClassification,
        request: CardReadOutput,
        transactionDateTime: Date
    ): Flow<Resource<ByteArray>> {
        return flow {
            val cardNo = request.cardNo
            val processingCode =
                if (cardClassification == CardClassification.DEBIT) SALE_DEBIT_PROCODE else SALE_CREDIT_PROCODE
            val amount = request.txnAmount.padAmount()
            val transactionDateTime = DateUtils.getFullTransactionDateTime(transactionDateTime)
            val stan = request.STAN.padStart(6, '0')
            val time = DateUtils.getTransactionTime(transactionDateTime)
            val date = DateUtils.getTransactionDate(transactionDateTime)
            val expiry = request.cardExpiry.replace("/", "")
            val posEntryMode = request.posEntryMode + "1"
            val panSeq = request.PANSEQ + "1"
            val nii = "041"
            val posConditionCode = "00"
            val track2 = request.track2Data.dropLast(1)
            val rrn = "000051000004"
            val approvalCode = "030059"
            val responseCode = "00"
            val tid = "12345678"
            val mid = "123456789012345"
            val pinBlock = request.pinBlock
            val iccData = request.emvData
            val fld57 = "1".repeat(300)
            val transactionDetails = "2".repeat(300)
            val fld62 = "0006123456789012"
            val messageAuthCode = "0006123456789012"

            try {
                emit(Resource.Loading("Mengirim data transaksi"))

                reversalManager.saveSaleReversal(
                    if (cardClassification == CardClassification.DEBIT) {
                        ReversalDebitRequestDto(
                            pan = cardNo,
                            processingCode = processingCode,
                            amount = amount,
                            transactionDateTime = transactionDateTime,
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
                    emit(Resource.Error("Error saat membuat request sale"))
                    return@flow
                }

                isoRepository.sendAndReceive(packedData).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            emit(Resource.Loading("Mengirim request sale..."))
                        }

                        is Resource.Success -> {
                            traceNumberManager.increment()
                            emit(Resource.Success(response.data ?: byteArrayOf()))
                        }

                        is Resource.Error -> {
                            emit(Resource.Error(response.message ?: "Terjadi kesalahan"))
                        }
                    }
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, "postSaleTransaction: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }

    override fun preparePrintingData(
        request: CardReadOutput, transactionDateTime: Date
    ): PrintBasedOnTemplateParameter {
        val amountText =
            StringUtil.formatRupiahCurrency((request.txnAmount.toLong() / 100).toString())
        val authCode = "711162"
        val refNo = "000047111620000"

        val currentTraceNoText = traceNumberManager.getCurrentLastTraceNo().toString().padStart(6, '0')
        val currentBatchNoText = terminalBatchManager.getCurrentBatch().toString().padStart(6, '0')

        val printFactory = SalePrintTemplateFactory(
            branchName = "DUMMY TRX",
            branchAddress = "JL. JENDRAL SUDIRMAN",
            branchCity = "JAKARTA",
            terminalId = "1234567890",
            merchantId = "1234567890",
            cardType = request.cardAppName,
            exp = request.cardExpiry,
            cardNumber = request.cardNo,
            cardMethod = CardUtil.getCardMethodFromPosEntryMode(request.posEntryMode),
            date = DateUtils.getReceiptTransactionDate(transactionDateTime),
            time = DateUtils.getReceiptTransactionTime(transactionDateTime),
            batch = currentBatchNoText,
            trace = currentTraceNoText,
            ref = refNo,
            appr = authCode,
            amount = amountText,
            version = "V2019.1.0.0.8",
        )

        val printTemplate = printFactory.getPrintBasedOnTemplateParameter { }

        return printTemplate
    }

    override suspend fun insertCardTransactionToDatabase(transactionRecord: TransactionRecord): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading("Menyimpan data transaksi"))
            try {
                with(transactionRecord) {
                    val cardTransactionEntity = CardTransactionEntity(
                        lastInvoice = lastInvoice,
                        lastInvoiceDate = lastInvoiceDate,
                        invoice = invoice,
                        invoiceDate = invoiceDate,
                        issuerID = issuerID,
                        issuerName = issuerName,
                        saleType = saleType,
                        batchNo = batchNo,
                        authCode = authCode,
                        amount = amount,
                        payID = payID,
                        pan = pan,
                        track2Data = track2Data,
                        mID = mID,
                        tID = tID,
                        printFormats = printFormats,
                        refNo = refNo,
                        txnTypeId = txnTypeId,
                        programName = programName,
                        cardExpiry = cardExpiry,
                        cardAID = cardAID,
                        cardAppName = cardAppName,
                        cardBinType = cardBinType,
                        cardClassificationType = cardClassificationType,
                        transactionScope = transactionScope,
                        nii = nii,
                        customerName = customerName,
                        currencyCode = currencyCode,
                        tVRData = tVRData,
                        tSIData = tSIData,
                        txnCatCode = txnCatCode,
                        txnCert = txnCert,
                        stan = stan,
                        maskedCardNo = maskedCardNo,
                        insertModeCode = insertModeCode,
                        rrNo = rrNo,
                        txnStatus = txnStatus,
                        cardType = cardType,
                        cardTypeCode = cardTypeCode,
                        acquiringBank = acquiringBank,
                        secureData = secureData,
                        posEntryMode = posEntryMode,
                        tipAmount = tipAmount,
                        cashAMT = cashAMT,
                        feeAmount = feeAmount,
                        refTxnTypeId = refTxnTypeId,
                        tenure = tenure,
                        bankTID = bankTID,
                        bankMID = bankMID,
                        cashierID = cashierID,
                        terminalCapability = terminalCapability,
                        jsonReq = jsonReq,
                        jsonResp = jsonResp,
                        eMIAmount = eMIAmount,
                        redeemAmount = redeemAmount,
                        pinBlock = pinBlock,
                        isTxnActive = isTxnActive,
                        isTxnVoid = isTxnVoid,
                        isVoidApplicable = isVoidApplicable,
                        isLoyaltyVoid = isLoyaltyVoid,
                        loyaltyData = loyaltyData,
                        panSeq = panSeq,
                        iccData = iccData,
                        responseCode = responseCode,
                        mti = mti,
                        jsonReceipt = jsonReceipt,
                        templateJsonReceipt = templateJsonReceipt
                    )

                    appDatabase.cardTransactionDao().insert(cardTransactionEntity)
                    emit(Resource.Success(Unit))
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, "insertCardTransactionToDatabase: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }

    override suspend fun printReceiptBasedLastTraceNo(): Flow<Resource<Unit>> {
        return printReceiptBasedTraceNo(
            traceNumberManager.getCurrentLastTraceNo()
                .toString()
                .padStart(6, '0')
        )
    }

    override suspend fun printReceiptBasedTraceNo(traceNo: String): Flow<Resource<Unit>> {
        return flow {
            try {
                emit(Resource.Loading("Mencari data"))

                Log.d(TAG, "printReceiptBasedTraceNo current: $traceNo")

                val cardTransactionEntity =
                    appDatabase.cardTransactionDao().getTrxDataByTraceNo(traceNo)

                if (cardTransactionEntity == null) {
                    emit(Resource.Error("Data tidak ditemukan"))
                    return@flow
                }
                emit(Resource.Loading("Mencetak struk"))
                printRepository.printWithBuilder(
                    builder = PrintBasedOnTemplateParameterBuilder().fromJson(
                        valueComponentJsonString = cardTransactionEntity.jsonReceipt,
                        templateComponentJsonString = cardTransactionEntity.templateJsonReceipt
                    )
                ).collect()
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                LogSdk.error(TAG, "printReceiptBasedTraceNo: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }

    override suspend fun postSaleReversal(): Flow<Resource<ByteArray>> {
        return flow {
            try {
                val reversalData = reversalManager.getSaleReversal()

                if (reversalData == null) {
                    emit(Resource.Error("Data reversal tidak ditemukan"))
                    return@flow
                }

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
                LogSdk.error(TAG, "postSaleReversal: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }
}