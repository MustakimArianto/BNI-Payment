package id.co.integrapratama.sdk.feature_installment.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.iso8583.IsoConfig
import id.co.integrapratama.sdk.core.utils.CardUtil
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.StringUtil
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.feature_installment.common.InstallmentPrintTemplateFactory
import id.co.integrapratama.sdk.feature_installment.data.local.InstallmentCardTransactionEntity
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPeriodModel
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPlanModel
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentRepository
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderValueComponent
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import id.co.payment2go.terminalsdkhelper.core.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Date

class InstallmentRepositoryImpl(
    private val isoRepository: Iso8583Repository,
    private val printRepository: PrintRepository,
    private val appDatabase: AppDatabase,
    private val traceNumberManager: TraceNumberManager,
    private val terminalBatchManager: TerminalBatchManager,
    private val deviceManagerUtility: DeviceManagerUtility,
    private val stanManager: StanManager
) : InstallmentRepository {
    companion object {
        private const val TAG = "InstallmentRepositoryImpl"
        private const val FINANCIAL_REQUEST_MTI = "0200"
        private const val PAYMENT_FROM_SAVING_PROCODE = "501000"
        private const val PAYMENT_FROM_CHECKING_PROCODE = "502000"
    }

    override suspend fun getInstallmentPeriodList(): Flow<Resource<List<InstallmentPeriodModel>>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        listOf(
                            InstallmentPeriodModel(
                                id = "1",
                                name = "3 Bulan"
                            ),
                            InstallmentPeriodModel(
                                id = "2",
                                name = "6 Bulan"
                            ),
                            InstallmentPeriodModel(
                                id = "3",
                                name = "12 Bulan"
                            ),
                            InstallmentPeriodModel(
                                id = "4",
                                name = "18 Bulan"
                            ),
                            InstallmentPeriodModel(
                                id = "5",
                                name = "24 Bulan"
                            )
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> emit(Resource.Error("Tidak ada koneksi Internet"))
                    is ConnectException -> emit(Resource.Error("Tidak dapat terhubung ke server"))
                    is SocketTimeoutException -> emit(Resource.Error("Koneksi Timeout"))
                    else -> emit(Resource.Error(e.message ?: "Unknown error occurred"))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getInstallmentPlanList(): Flow<Resource<List<InstallmentPlanModel>>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        listOf(
                            InstallmentPlanModel(
                                id = "1",
                                name = "Plan 1"
                            ),
                            InstallmentPlanModel(
                                id = "2",
                                name = "Plan 2"
                            ),
                            InstallmentPlanModel(
                                id = "3",
                                name = "Plan 3"
                            ),
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> emit(Resource.Error("Tidak ada koneksi Internet"))
                    is ConnectException -> emit(Resource.Error("Tidak dapat terhubung ke server"))
                    is SocketTimeoutException -> emit(Resource.Error("Koneksi Timeout"))
                    else -> emit(Resource.Error(e.message ?: "Unknown error occurred"))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun test() {
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "BranchName",
            value = "BRI 1"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "BranchAddress",
            value = "JL. JENDRAL SUDIRMAN"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "BranchCity",
            value = "JAKARTA"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "TerminalId",
            value = "1234567890"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "MerchantId",
            value = "1234567890"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "CardType",
            value = "BRI MASTERCARD"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Exp",
            value = "3/23"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "CartNumberWithType",
            value = "6013*******3881 (SWIPE)"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "SaleLabel",
            value = "SALE"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "SaleDate",
            value = "5 FEB 2020"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "SaleTime",
            value = "13:31:09"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "SaleBatch",
            value = "000002"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "SaleTrace",
            value = "000008"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "SaleRef",
            value = "000047111620000"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "SaleAppr",
            value = "711162"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "SaleAmount",
            value = "Rp. 20500"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "PinVerificationSuccessLabel",
            value = "*** PIN VERIFICATION SUCCESS ***"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "TotalAmountAgreementLabel",
            value = "I AGREE TO PAY ABOVE TOTAL AMOUNT"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "AccordingToCardLabel",
            value = "ACCORDING TO CARD ISSUER AGREEMENT"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "CopyLabel",
            value = "*** CUSTOMER COPY ***"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Version",
            value = "V2019.1.0.0.8"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "MachineSerialNumber",
            value = "14169CT22114673"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Line",
            value = ""
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "HeaderImage",
            value = "integra-pratama.png"
        )
        PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "QrisContent",
            value = "00020101021126680016ID.CO.TELKOM.WWW011893600898029183533402150001952918353340303UMI51440014ID.CO.QRIS.WWW0215ID10232753331940303UMI5204549953033605502015802ID5913Samuel Mareno6013KOTA SEMARANG61055026562220511893318458550703A106304A92B"
        )
    }

    override suspend fun postInstallmentTransaction(
        isFromSaving: Boolean,
        request: CardReadOutput,
        transactionDateTime: String
    ): Flow<Resource<ByteArray>> {
        return flow {
            try {
                emit(Resource.Loading("Harap tunggu"))
                val requestData = mapOf(
                    3 to if (isFromSaving) PAYMENT_FROM_SAVING_PROCODE else PAYMENT_FROM_CHECKING_PROCODE,
                    4 to request.txnAmount.padAmount(),
                    7 to transactionDateTime,
                    11 to request.STAN.padStart(6, '0'),
                    12 to DateUtils.getTransactionTime(transactionDateTime), // Time
                    13 to DateUtils.getTransactionDate(transactionDateTime), // Date
                    18 to "0125", // Merchant Type
                    22 to request.posEntryMode + "1",
                    24 to "041", // NII
                    32 to "1234567890123456789012", // Acquiring Institution ID
                    35 to request.track2Data,
                    41 to "12345678", // TID
                    42 to "123456789012345", // MID
                    48 to "01551111000000000000000000000000000000000000000000000000000000000000111111111111111111111111111111namenamenamenamenamenamenamenamenamenamenamenamenamenamename11110099999999777777777777777777777777777777777777777777777777777777777777888888888888888888883333333333333333333344444444444444444444666666666666666666", // CSM Data
                    52 to request.pinBlock,
                    55 to request.emvData
                )
                val packedData = isoRepository.createRequest(
                    FINANCIAL_REQUEST_MTI,
                    requestData,
                    IsoConfig.genericSpec
                )

                if (packedData.isEmpty()) {
                    emit(Resource.Error("Error saat membuat request installment"))
                    return@flow
                }

                isoRepository.sendAndReceive(packedData).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            emit(Resource.Loading("Mengirim request installment...", null))
                        }

                        is Resource.Success -> {
                            val currentDate = Date()
                            val currentTraceNoText = Util.addZerosToNumber(
                                traceNumberManager.getCurrentTraceNo(),
                                desiredDigits = 6
                            )
                            val currentBatchNoText = Util.addZerosToNumber(
                                terminalBatchManager.getCurrentBatch(),
                                desiredDigits = 6
                            )
                            val currentStanText = Util.addZerosToNumber(
                                stanManager.getCurrentStan(),
                                desiredDigits = 6
                            )
                            val amountText = StringUtil.formatRupiahCurrency(request.txnAmount)
                            val authCode = "711162"
                            val refNo = "000047111620000"
                            val installmentPrintTemplateFactory = InstallmentPrintTemplateFactory(
                                branchName = "DUMMY TRX",
                                branchAddress = "JL. JENDRAL SUDIRMAN",
                                branchCity = "JAKARTA",
                                terminalId = "1234567890",
                                merchantId = "1234567890",
                                cardType = request.cardAppName,
                                exp = request.cardExpiry,
                                cardNumber = request.cardNo,
                                cardMethod = CardUtil.getCardMethodFromPosEntryMode(request.posEntryMode),
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
                            val installmentPrintBasedOnTemplateParameter =
                                installmentPrintTemplateFactory.getPrintBasedOnTemplateParameter {}
                            appDatabase.installmentCardTransactionDao().insert(
                                InstallmentCardTransactionEntity(
                                    invoice = currentTraceNoText,
                                    invoiceDate = DateUtils.getFullTransactionDateTime(currentDate),
                                    issuerID = "",
                                    issuerName = "",
                                    saleType = "",
                                    batchNo = currentTraceNoText,
                                    authCode = authCode,
                                    amount = request.txnAmount.toLongOrNull() ?: 0,
                                    payID = "",
                                    pan = request.cardNo,
                                    mID = "",
                                    tID = "",
                                    printFormats = "",
                                    refNo = refNo,
                                    txnTypeId = "",
                                    programName = "",
                                    cardExpiry = request.cardExpiry,
                                    cardAID = request.cardAID,
                                    cardAppName = request.cardAppName,
                                    customerName = request.customerName,
                                    currencyCode = request.currencyCode,
                                    tVRData = request.terminalVerificationResults,
                                    tSIData = request.TSIData,
                                    txnCatCode = request.txnCategoryCode,
                                    txnCert = request.transactionCertificate,
                                    stan = currentStanText,
                                    maskedCardNo = StringUtil.formatRupiahCurrency(request.cardNo),
                                    insertModeCode = request.insertModeCode,
                                    rRNO = refNo,
                                    txnStatus = "Success",
                                    cardType = "",
                                    cardTypeCode = "",
                                    acquiringBank = "",
                                    secureData = "",
                                    posEntryMode = request.posEntryMode,
                                    tipAmount = 0,
                                    cashAMT = 0,
                                    feeAmount = 0,
                                    refTxnTypeId = "",
                                    tenure = "",
                                    bankTID = "",
                                    bankMID = "",
                                    cashierID = "",
                                    terminalCapability = request.terminalCapability,
                                    jsonReq = "",
                                    jsonResp = "",
                                    eMIAmount = 0L,
                                    redeemAmount = 0L,
                                    pinBlock = request.pinBlock,
                                    isTxnActive = false,
                                    isTxnVoid = false,
                                    isVoidApplicable = false,
                                    isLoyaltyVoid = false,
                                    loyaltyData = "",
                                    panSeq = request.PANSEQ,
                                    iccData = request.emvData,
                                    responseCode = "",
                                    mti = "",
                                    jsonReceipt = installmentPrintBasedOnTemplateParameter.jsonString,
                                    templateJsonReceipt = installmentPrintBasedOnTemplateParameter.printTemplateJsonString
                                )
                            )
                            traceNumberManager.increment()
                            stanManager.increaseStan()
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
                    else -> emit(Resource.Error(e.message ?: "Unknown error occurred"))
                }
            }
        }
    }

    override suspend fun printReceiptBasedLastTraceNo(): Flow<Resource<Unit>> {
        return printReceiptBasedTraceNo(
            Util.addZerosToNumber(
                traceNumberManager.getCurrentLastTraceNo(),
                desiredDigits = 6
            )
        )
    }

    override suspend fun printReceiptBasedTraceNo(
        traceNo: String
    ): Flow<Resource<Unit>> {
        return flow {
            try {
                emit(Resource.Loading("Mencari data"))
                val installmentCardTransactionEntity = appDatabase.installmentCardTransactionDao()
                    .getTrxDataByTraceNo(traceNo)
                if (installmentCardTransactionEntity == null) {
                    emit(Resource.Error("Data tidak ditemukan"))
                    return@flow
                }
                emit(Resource.Loading("Mencetak struk"))
                printRepository.printWithBuilder(
                    builder = PrintBasedOnTemplateParameterBuilder().fromJson(
                        valueComponentJsonString = installmentCardTransactionEntity.jsonReceipt,
                        templateComponentJsonString = installmentCardTransactionEntity.templateJsonReceipt
                    )
                ).collect()
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                LogSdk.error(TAG, e.stackTraceToString())
                when (e) {
                    is UnknownHostException -> emit(Resource.Error("Tidak ada koneksi Internet"))
                    is ConnectException -> emit(Resource.Error("Tidak dapat terhubung ke server"))
                    is SocketTimeoutException -> emit(Resource.Error("Koneksi Timeout"))
                    else -> emit(Resource.Error(e.message ?: "Unknown error occurred"))
                }
            }
        }
    }
}