package id.co.integrapratama.sdk.feature_settlement.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.model.MTI
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.core.utils.toResourceError
import id.co.integrapratama.sdk.feature_bin_range.domain.BinRangeRepository
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.integrapratama.sdk.feature_sale.data.local.CardTransactionEntity
import id.co.integrapratama.sdk.feature_settlement.core.SettlementPrintTemplateFactory
import id.co.integrapratama.sdk.feature_settlement.domain.PostSettlementAndBatchUploadResponseModel
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementRepository
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementSubSummaryModel
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementSummaryGroupModel
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementSummaryModel
import id.co.integrapratama.sdk.feature_settlement.domain.TotalSettlementSummaryModel
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Date

class SettlementRepositoryImpl(
    private val db: AppDatabase,
    private val batchManager: TerminalBatchManager,
    private val traceNumberManager: TraceNumberManager,
    private val stanManager: StanManager,
    private val isoRepository: Iso8583Repository,
    private val printRepository: PrintRepository,
    private val binRangeRepository: BinRangeRepository
) : SettlementRepository {
    companion object {
        private const val TAG = "SettlementRepositoryImpl"
        private const val SETTLEMENT_DEBIT_INITIAL_PROCODE = "920000"
        private const val SETTLEMENT_DEBIT_FINAL_PROCODE = "960000"
        private const val SETTLEMENT_CREDIT_PROCODE = "920000"
        private const val BATCH_UPLOAD_PROCODE = "970000"
    }

    override fun getTotalSettlementSummary(): Flow<Resource<TotalSettlementSummaryModel>> {
        return flow {
            try {
                emit(Resource.Loading())

                // Get all trx from card transaction
                val cardTransactionEntityList = db.cardTransactionDao().getAllTrxData()

                var totalSettlementSummaryModel = TotalSettlementSummaryModel(
                    totalSale = 0,
                    totalVoid = 0,
                    totalRefund = 0
                )

                cardTransactionEntityList.forEach { cardTransactionEntity ->
                    val saleType = cardTransactionEntity.saleType.lowercase()
                    when (saleType) {
                        "sale" -> {
                            totalSettlementSummaryModel = totalSettlementSummaryModel.copy(
                                totalSale = totalSettlementSummaryModel.totalSale + 1
                            )
                        }
                        "void" -> {
                            totalSettlementSummaryModel = totalSettlementSummaryModel.copy(
                                totalVoid = totalSettlementSummaryModel.totalVoid + 1
                            )
                        }
                        "refund" -> {
                            totalSettlementSummaryModel = totalSettlementSummaryModel.copy(
                                totalRefund = totalSettlementSummaryModel.totalRefund + 1
                            )
                        }
                    }
                }

                totalSettlementSummaryModel = totalSettlementSummaryModel.copy(
                    totalVoid = -totalSettlementSummaryModel.totalVoid,
                    totalRefund = -totalSettlementSummaryModel.totalRefund
                )

                emit(
                    Resource.Success(
                        totalSettlementSummaryModel
                    )
                )
            } catch (e: Exception) {
                LogSdk.error(TAG, "getTotalSettlementSummary: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun postSettlementAndBatchUpload(): Flow<Resource<PostSettlementAndBatchUploadResponseModel>> {
        return flow {
            try {
                val currentDate = Date()

                emit(Resource.Loading("Melakukan Settlement"))

                // Get all trx from card transaction
                val cardTransactionEntityList = db.cardTransactionDao().getAllTrxData()

                // Settlement
                val processingCode = SETTLEMENT_DEBIT_INITIAL_PROCODE

                val requestData = mapOf(
                    2 to "1234567890123456",
                    3 to processingCode,
                    4 to "1".padAmount(),
                    11 to "1".padStart(6, '0'),
                    14 to "00/00".replace("/", ""),
                    22 to "05" + "1",
                    24 to "041", // NII
                    25 to "00", // NII
                    35 to "1234567890123456D12345678",
                    37 to "000051000004", // RRN
                    38 to "030059", // Approval Code
                    41 to "12345678", // TID
                    42 to "123456789012345", // MID
                    52 to "FFFFFFFFFFFFFFFF",
                    55 to "9F0203000000",
                    62 to "0006123456789012" // Private Use - Invoice or ECR Reference Number
                )

                val packedData = isoRepository.createRequest(
                    MTI.SETTLEMENT.code,
                    requestData,
                )

                isoRepository.sendAndReceive(packedData).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            emit(Resource.Loading("Melakukan settlement"))
                        }

                        is Resource.Success -> {
                            // Batch Upload
                            val processingCode = BATCH_UPLOAD_PROCODE

                            val requestData = mapOf(
                                2 to "1234567890123456",
                                3 to processingCode,
                                4 to "1".padAmount(),
                                11 to "1".padStart(6, '0'),
                                14 to "00/00".replace("/", ""),
                                22 to "05" + "1",
                                24 to "041", // NII
                                25 to "00", // NII
                                35 to "1234567890123456D12345678",
                                37 to "000051000004", // RRN
                                38 to "030059", // Approval Code
                                41 to "12345678", // TID
                                42 to "123456789012345", // MID
                                52 to "FFFFFFFFFFFFFFFF",
                                55 to "9F0203000000",
                                62 to "0006123456789012" // Private Use - Invoice or ECR Reference Number
                            )

                            val packedData = isoRepository.createRequest(
                                MTI.BATCH_UPLOAD.code,
                                requestData,
                            )

                            isoRepository.sendAndReceive(packedData).collect { response ->
                                when (response) {
                                    is Resource.Loading -> {
                                        emit(Resource.Loading("Melakukan settlement"))
                                    }

                                    is Resource.Success -> {
                                        preparePrintSettlement(
                                            cardTransactionEntityList = cardTransactionEntityList,
                                            currentDate = currentDate,
                                        ) {
                                            this.emit(it)
                                        }
                                    }

                                    is Resource.Error -> {
                                        emit(Resource.Error(response.message ?: "Terjadi kesalahan"))
                                    }
                                }
                            }
                        }

                        is Resource.Error -> {
                            emit(Resource.Error(response.message ?: "Terjadi kesalahan"))
                        }
                    }
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, "postSettlementAndBatchUpload: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun preparePrintSettlement(
        cardTransactionEntityList: List<CardTransactionEntity>,
        currentDate: Date,
        onUpdate: suspend (Resource<PostSettlementAndBatchUploadResponseModel>) -> Unit
    ) {
        val settlementSummaryGroupModelList = mutableListOf<SettlementSummaryGroupModel>()

        fun getSettlementSummaryGroupModel(
            title: String,
            desiredCardClassificationType: String
        ): SettlementSummaryGroupModel {
            return SettlementSummaryGroupModel(
                title = title,
                summaryModelList = fun (): MutableList<SettlementSummaryModel> {
                    val settlementSummaryModelList = mutableListOf<SettlementSummaryModel>()
                    val cardTransactionEntityList = cardTransactionEntityList.filter {
                        it.cardClassificationType?.lowercase() == desiredCardClassificationType.lowercase()
                    }
                    val cardTransactionEntityGroupMap = cardTransactionEntityList.groupBy {
                        it.cardBinType ?: ""
                    }
                    if (cardTransactionEntityGroupMap.isEmpty()) {
                        settlementSummaryModelList.add(
                            SettlementSummaryModel(
                                title = "OTHER",
                                subSummaryList = fun (): MutableList<SettlementSubSummaryModel> {
                                    val subSummaryModelList = mutableListOf<SettlementSubSummaryModel>()

                                    // SALE
                                    val saleSettlementSubSummaryModel = SettlementSubSummaryModel(
                                        title = "SALE",
                                        count = 0,
                                        amount = 0
                                    )
                                    subSummaryModelList.add(saleSettlementSubSummaryModel)

                                    // VOID
                                    val voidSettlementSubSummaryModel = SettlementSubSummaryModel(
                                        title = "VOID",
                                        count = 0,
                                        amount = 0
                                    )
                                    subSummaryModelList.add(voidSettlementSubSummaryModel)

                                    // TOTAL
                                    val totalSettlementSubSummaryModel = SettlementSubSummaryModel(
                                        title = "TOTAL",
                                        amount = 0
                                    )
                                    subSummaryModelList.add(totalSettlementSubSummaryModel)

                                    return subSummaryModelList
                                }()
                            )
                        )
                    } else {
                        cardTransactionEntityGroupMap.forEach { key, value ->
                            val effectiveKey = key.ifBlank { "OTHER" }
                            settlementSummaryModelList.add(
                                SettlementSummaryModel(
                                    title = effectiveKey,
                                    subSummaryList = fun(): MutableList<SettlementSubSummaryModel> {
                                        val subSummaryModelList =
                                            mutableListOf<SettlementSubSummaryModel>()

                                        // SALE
                                        val saleCardTransactionEntityList = value.filter {
                                            it.saleType.lowercase() == "sale"
                                        }
                                        val saleSettlementSubSummaryModel = SettlementSubSummaryModel(
                                            title = "SALE",
                                            count = saleCardTransactionEntityList.size,
                                            amount = saleCardTransactionEntityList.sumOf { it.amount / 100 }
                                        )
                                        subSummaryModelList.add(saleSettlementSubSummaryModel)

                                        // VOID
                                        val voidCardTransactionEntityList = value.filter {
                                            it.saleType.lowercase() == "void"
                                        }
                                        val voidSettlementSubSummaryModel = SettlementSubSummaryModel(
                                            title = "VOID",
                                            count = voidCardTransactionEntityList.size,
                                            amount = -voidCardTransactionEntityList.sumOf { it.amount / 100 }
                                        )
                                        subSummaryModelList.add(voidSettlementSubSummaryModel)

                                        // TOTAL
                                        val totalSettlementSubSummaryModel = SettlementSubSummaryModel(
                                            title = "TOTAL",
                                            amount = saleSettlementSubSummaryModel.amount + voidSettlementSubSummaryModel.amount
                                        )
                                        subSummaryModelList.add(totalSettlementSubSummaryModel)

                                        return subSummaryModelList
                                    }()
                                )
                            )
                        }
                    }

                    // Grand Total
                    settlementSummaryModelList.add(
                        SettlementSummaryModel(
                            title = "GRAND TOTAL",
                            subSummaryList = fun (): MutableList<SettlementSubSummaryModel> {
                                val subSummaryModelList = mutableListOf<SettlementSubSummaryModel>()

                                // SALE
                                val saleCardTransactionEntityList = cardTransactionEntityList.filter {
                                    it.saleType.lowercase() == "sale"
                                }
                                val saleSettlementSubSummaryModel = SettlementSubSummaryModel(
                                    title = "SALE",
                                    count = saleCardTransactionEntityList.size,
                                    amount = saleCardTransactionEntityList.sumOf { it.amount / 100 }
                                )
                                subSummaryModelList.add(saleSettlementSubSummaryModel)

                                // VOID
                                val voidCardTransactionEntityList = cardTransactionEntityList.filter {
                                    it.saleType.lowercase() == "void"
                                }
                                val voidSettlementSubSummaryModel = SettlementSubSummaryModel(
                                    title = "VOID",
                                    count = voidCardTransactionEntityList.size,
                                    amount = -voidCardTransactionEntityList.sumOf { it.amount / 100 }
                                )
                                subSummaryModelList.add(voidSettlementSubSummaryModel)

                                // TOTAL
                                val totalSettlementSubSummaryModel = SettlementSubSummaryModel(
                                    title = "TOTAL",
                                    amount = saleSettlementSubSummaryModel.amount + voidSettlementSubSummaryModel.amount
                                )
                                subSummaryModelList.add(totalSettlementSubSummaryModel)

                                return subSummaryModelList
                            }()
                        )
                    )

                    return settlementSummaryModelList
                }()
            )
        }

        // Debit Card
        val debitSettlementSummaryGroupModel = getSettlementSummaryGroupModel(
            title = "DEBIT CARD",
            desiredCardClassificationType = "debit"
        )
        settlementSummaryGroupModelList.add(debitSettlementSummaryGroupModel)

        // Credit Card
        val creditSettlementSummaryGroupModel = getSettlementSummaryGroupModel(
            title = "CREDIT CARD",
            desiredCardClassificationType = "credit"
        )
        settlementSummaryGroupModelList.add(creditSettlementSummaryGroupModel)

        val settlementPrintTemplateFactory = SettlementPrintTemplateFactory(
            branchName = "DUMMY TRX",
            branchAddress = "JL. JENDRAL SUDIRMAN",
            branchCity = "JAKARTA",
            terminalId = "1234567890",
            merchantId = "1234567890",
            date = DateUtils.getReceiptTransactionDate(currentDate),
            time = DateUtils.getReceiptTransactionTime(currentDate),
            batch = batchManager.getCurrentBatch().toString().padStart(6, '0'),
            settlementSummaryGroupModelList = settlementSummaryGroupModelList
        )
        val settlementPrintBasedOnTemplateParameter = settlementPrintTemplateFactory.getPrintBasedOnTemplateParameter {}

        db.cardTransactionDao().moveAllTrxToPreviousBatch()
        traceNumberManager.reset()
        batchManager.reset()
        stanManager.resetStan()

        onUpdate(
            Resource.Success(
                PostSettlementAndBatchUploadResponseModel(
                    isoResponse = byteArrayOf(),
                    settlementPrintBasedOnTemplateParameter = settlementPrintBasedOnTemplateParameter
                )
            )
        )
    }

    override fun printSettlement(settlementPrintBasedOnTemplateParameter: PrintBasedOnTemplateParameter): Flow<Resource<Unit>> {
        return flow {
            try {
                emit(Resource.Loading("Mencetak struk"))
                printRepository.printWithBuilder(
                    PrintBasedOnTemplateParameterBuilder().fromJson(
                        valueComponentJsonString = settlementPrintBasedOnTemplateParameter.jsonString,
                        templateComponentJsonString = settlementPrintBasedOnTemplateParameter.printTemplateJsonString
                    )
                ).collect {
                    if (it is Resource.Loading) {
                        return@collect
                    }
                    emit(it)
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, "printSettlement: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }
}