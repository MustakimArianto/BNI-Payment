package id.co.integrapratama.sdk.feature_settlement.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.utils.toResourceError
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementRepository
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementSummaryModel
import id.co.integrapratama.sdk.feature_settlement.domain.TotalSettlementSummaryModel
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SettlementRepositoryImpl(
    private val db: AppDatabase,
    private val isoRepository: Iso8583Repository,
    private val printRepository: PrintRepository,
    private val traceNumberManager: TraceNumberManager,
    private val batchManager: TerminalBatchManager,
    private val stanManager: StanManager,
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
                LogSdk.error(TAG, "postSettlement: ${e.message}")
                emit(e.toResourceError())
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun postSettlementAndBatchUpload(): Flow<Resource<ByteArray>> {
        return flow {
            try {
                emit(Resource.Loading())

                val settlementSummaryModelList = mutableListOf<SettlementSummaryModel>()

                // Get all trx from card transaction
                val cardTransactionEntityList = db.cardTransactionDao().getAllTrxData()

                emit(
                    Resource.Success(byteArrayOf())
                )
            } catch (e: Exception) {
                LogSdk.error(TAG, "postSettlement: ${e.message}")
                emit(e.toResourceError())
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun printSettlement(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(Unit)
                )
            } catch (e: Exception) {
                LogSdk.error(TAG, "postBatchUpload: ${e.message}")
                emit(e.toResourceError())
            }
        }
    }
}