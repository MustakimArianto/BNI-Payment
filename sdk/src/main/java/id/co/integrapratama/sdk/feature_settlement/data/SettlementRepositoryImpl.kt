package id.co.integrapratama.sdk.feature_settlement.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.toResourceError
import id.co.integrapratama.sdk.feature_bin_range.domain.BinRangeRepository
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
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
        private const val SETTLEMENT_REQUEST_MTI = "0200"
        private const val SETTLEMENT_DEBIT_INITIAL_PROCODE = "920000"
        private const val SETTLEMENT_DEBIT_FINAL_PROCODE = "960000"
        private const val SETTLEMENT_CREDIT_PROCODE = "920000"
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

                emit(
                    Resource.Success(
                        totalSettlementSummaryModel
                    )
                )
            } catch (e: Exception) {
                LogSdk.error(TAG, "getTotalSettlementSummary: ${e.message}")
                emit(e.toResourceError())
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun postSettlementAndBatchUpload(): Flow<Resource<PostSettlementAndBatchUploadResponseModel>> {
        return flow {
            try {
                val currentDate = Date()

                emit(Resource.Loading())

                val settlementSummaryGroupModelList = mutableListOf<SettlementSummaryGroupModel>()

                // Get all trx from card transaction
                val cardTransactionEntityList = db.cardTransactionDao().getAllTrxData()

                // Debit Card
                val debitSettlementSummaryGroupModel = SettlementSummaryGroupModel(
                    title = "DEBIT CARD",
                    summaryModelList = fun (): MutableList<SettlementSummaryModel> {
                        val settlementSummaryModelList = mutableListOf<SettlementSummaryModel>()
                        val debitCardTransactionEntityList = cardTransactionEntityList.filter {
                            it.cardClassificationType?.lowercase() == "debit"
                        }
                        val debitCardTransactionEntityGroupMap = debitCardTransactionEntityList.groupBy {
                            it.cardBinType ?: ""
                        }
                        if (debitCardTransactionEntityGroupMap.isEmpty()) {
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
                            debitCardTransactionEntityGroupMap.forEach { key, value ->
                                settlementSummaryModelList.add(
                                    SettlementSummaryModel(
                                        title = key,
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
                                                amount = voidCardTransactionEntityList.sumOf { it.amount / 100 }
                                            )
                                            subSummaryModelList.add(voidSettlementSubSummaryModel)

                                            // TOTAL
                                            val totalSettlementSubSummaryModel = SettlementSubSummaryModel(
                                                title = "TOTAL",
                                                amount = saleSettlementSubSummaryModel.amount - voidSettlementSubSummaryModel.amount
                                            )
                                            subSummaryModelList.add(totalSettlementSubSummaryModel)

                                            return subSummaryModelList
                                        }()
                                    )
                                )
                            }
                        }
                        return settlementSummaryModelList
                    }()
                )
                settlementSummaryGroupModelList.add(debitSettlementSummaryGroupModel)

                // Credit Card
                val creditSettlementSummaryGroupModel = SettlementSummaryGroupModel(
                    title = "CREDIT CARD",
                    summaryModelList = fun (): MutableList<SettlementSummaryModel> {
                        val settlementSummaryModelList = mutableListOf<SettlementSummaryModel>()
                        val creditCardTransactionEntityList = cardTransactionEntityList.filter {
                            it.cardClassificationType?.lowercase() == "credit"
                        }
                        val creditCardTransactionEntityGroupMap = creditCardTransactionEntityList.groupBy {
                            it.cardBinType ?: ""
                        }
                        if (creditCardTransactionEntityGroupMap.isEmpty()) {
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
                            creditCardTransactionEntityGroupMap.forEach { key, value ->
                                settlementSummaryModelList.add(
                                    SettlementSummaryModel(
                                        title = key,
                                        subSummaryList = fun (): MutableList<SettlementSubSummaryModel> {
                                            val subSummaryModelList = mutableListOf<SettlementSubSummaryModel>()

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
                                                amount = voidCardTransactionEntityList.sumOf { it.amount / 100 }
                                            )
                                            subSummaryModelList.add(voidSettlementSubSummaryModel)

                                            // TOTAL
                                            val totalSettlementSubSummaryModel = SettlementSubSummaryModel(
                                                title = "TOTAL",
                                                amount = saleSettlementSubSummaryModel.amount - voidSettlementSubSummaryModel.amount
                                            )
                                            subSummaryModelList.add(totalSettlementSubSummaryModel)

                                            return subSummaryModelList
                                        }()
                                    )
                                )
                            }
                        }
                        return settlementSummaryModelList
                    }()
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

                traceNumberManager.reset()
                batchManager.reset()
                stanManager.resetStan()

                emit(
                    Resource.Success(
                        PostSettlementAndBatchUploadResponseModel(
                            isoResponse = byteArrayOf(),
                            settlementPrintBasedOnTemplateParameter = settlementPrintBasedOnTemplateParameter
                        )
                    )
                )
            } catch (e: Exception) {
                LogSdk.error(TAG, "postSettlementAndBatchUpload: ${e.message}")
                emit(e.toResourceError())
            }
        }.flowOn(Dispatchers.IO)
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
                LogSdk.error(TAG, "printSettlement: ${e.message}")
                emit(e.toResourceError())
            }
        }
    }
}