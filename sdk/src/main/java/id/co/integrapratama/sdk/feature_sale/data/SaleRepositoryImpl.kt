package id.co.integrapratama.sdk.feature_sale.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.core.utils.toResourceError
import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.integrapratama.sdk.feature_sale.data.local.CardTransactionEntity
import id.co.integrapratama.sdk.feature_sale.domain.SaleRepository
import id.co.integrapratama.sdk.feature_sale.domain.TransactionRecord
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val isoRepository: Iso8583Repository,
    private val db: AppDatabase,
) : SaleRepository {
    companion object {
        private const val TAG = "SaleRepositoryImpl"
        private const val FINANCIAL_REQUEST_MTI = "0200"
        private const val SALE_DEBIT_PROCODE = "000000"
        private const val SALE_CREDIT_PROCODE = "020000"
    }

    override suspend fun postSaleTransaction(
        cardClassification: CardClassification,
        request: CardReadOutput,
        transactionDateTime: String
    ): Flow<Resource<ByteArray>> {
        return flow {
            try {
                emit(Resource.Loading("Harap tunggu"))
                val requestData = if (cardClassification == CardClassification.DEBIT) {
                    mutableMapOf<Int, String>().apply {
                        put(2, request.cardNo)
                        put(3, SALE_DEBIT_PROCODE)
                        put(4, request.txnAmount.padAmount())
                        put(11, request.STAN.padStart(6, '0'))
                        put(14, request.cardExpiry.replace("/", ""))
                        put(22, request.posEntryMode + "1")
                        put(24, "041") // NII
                        put(25, "00") // NII
                        put(35, request.track2Data.dropLast(1))
                        put(37, "000051000004") // RRN
                        put(38, "030059") // Approval Code
                        put(41, "12345678") // TID
                        put(42, "123456789012345") // MID
                        put(52, request.pinBlock)
                        if (request.emvData.isNotEmpty()) {
                            put(55, request.emvData)
                        }
                        put(62, "0006123456789012") // Private Use - Invoice or ECR Reference Number
                    }
                } else {
                    mutableMapOf<Int, String>().apply {
                        put(3, SALE_CREDIT_PROCODE)
                        put(4, request.txnAmount.padAmount())
                        put(11, request.STAN.padStart(6, '0'))
                        put(12, DateUtils.getTransactionTime(transactionDateTime)) // Time
                        put(13, DateUtils.getTransactionDate(transactionDateTime)) // Date
                        put(22, request.posEntryMode + "1")
                        put(23, request.PANSEQ + "1")
                        put(24, "041") // NII
                        put(25, "00") // NII
                        put(35, request.track2Data.dropLast(1))
                        put(37, "000051000004") // RRN
                        put(38, "030059") // Approval Code
                        put(39, "00") // Approval Code
                        put(41, "12345678") // TID
                        put(42, "123456789012345") // MID
                        if (request.emvData.isNotEmpty()) {
                            put(55, request.emvData)
                        }
                        put(57, "1".repeat(300)) // Reserved From TLE
                        put(61, "2".repeat(300)) // Transaction Details
                        put(62, "2".repeat(300)) // Additional Data Private
                        put(64, "0006123456789012") // Private Use - Invoice or ECR Reference Number
                    }
                }

                val packedData = isoRepository.createRequest(
                    FINANCIAL_REQUEST_MTI,
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
                            emit(Resource.Success(response.data ?: byteArrayOf()))
                        }

                        is Resource.Error -> {
                            emit(Resource.Error(response.message ?: "Terjadi kesalahan"))
                        }
                    }
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, e.stackTraceToString())
                emit(e.toResourceError())
            }
        }
    }

    override suspend fun insertCardTransactionToDatabase(transactionRecord: TransactionRecord): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading("Menyimpan data transaksi"))
            try {
                with(transactionRecord) {
                    val cardTransactionEntity = CardTransactionEntity(
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
                        mID = mID,
                        tID = tID,
                        printFormats = printFormats,
                        refNo = refNo,
                        txnTypeId = txnTypeId,
                        programName = programName,
                        cardExpiry = cardExpiry,
                        cardAID = cardAID,
                        cardAppName = cardAppName,
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

                    db.cardTransactionDao().insert(cardTransactionEntity)
                    emit(Resource.Success(Unit))
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, e.stackTraceToString())
                emit(e.toResourceError())
            }
        }
    }
}