package id.co.integrapratama.sdk.feature_sale.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.integrapratama.sdk.feature_sale.data.local.CardTransactionEntity
import id.co.integrapratama.sdk.feature_sale.domain.SaleRepository
import id.co.integrapratama.sdk.feature_sale.domain.TransactionRecord
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
                    mapOf(
                        2 to request.cardNo,
                        3 to SALE_DEBIT_PROCODE,
                        4 to request.txnAmount.padAmount(),
                        11 to request.STAN.padStart(6, '0'),
                        14 to request.cardExpiry.replace("/", ""),
                        22 to request.posEntryMode + "1",
                        24 to "041", // NII
                        25 to "00", // NII
                        35 to request.track2Data,
                        37 to "000051000004", // RRN
                        38 to "030059", // Approval Code
                        41 to "12345678", // TID
                        42 to "123456789012345", // MID
                        52 to request.pinBlock,
                        55 to request.emvData,
                        62 to "0006123456789012" // Private Use - Invoice or ECR Reference Number
                    )
                } else {
                    mapOf(
                        3 to SALE_CREDIT_PROCODE,
                        4 to request.txnAmount.padAmount(),
                        11 to request.STAN.padStart(6, '0'),
                        12 to DateUtils.getTransactionTime(transactionDateTime), // Time
                        13 to DateUtils.getTransactionDate(transactionDateTime), // Date
                        22 to request.posEntryMode + "1",
                        23 to request.PANSEQ,
                        24 to "041", // NII
                        25 to "00", // NII
                        35 to request.track2Data,
                        37 to "000051000004", // RRN
                        38 to "030059", // Approval Code
                        39 to "00", // Approval Code
                        41 to "12345678", // TID
                        42 to "123456789012345", // MID
                        57 to "1".repeat(300), // Reserved From TLE
                        61 to "2".repeat(300), // Transaction Details
                        62 to "2".repeat(300), // Additional Data Private
                        64 to "0006123456789012" // Private Use - Invoice or ECR Reference Number
                    )
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
                when (e) {
                    is UnknownHostException -> emit(Resource.Error("Tidak ada koneksi Internet"))
                    is ConnectException -> emit(Resource.Error("Tidak dapat terhubung ke server"))
                    is SocketTimeoutException -> emit(Resource.Error("Koneksi Timeout"))
                    else -> emit(Resource.Error("Terjadi kesalahan"))
                }
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
                emit(Resource.Error("Terjadi kesalahan"))
            }
        }
    }
}