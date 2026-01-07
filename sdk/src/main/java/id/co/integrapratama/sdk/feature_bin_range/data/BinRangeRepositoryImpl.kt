package id.co.integrapratama.sdk.feature_bin_range.data

import android.database.sqlite.SQLiteException
import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.feature_bin_range.domain.BinRangeRepository
import id.co.integrapratama.sdk.feature_bin_range.domain.BinType
import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BinRangeRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : BinRangeRepository {
    companion object {
        const val TAG = "BinRangeRepositoryImpl"
    }

    override suspend fun getBinType(cardNumber: String): Flow<Resource<BinType>> {
        return flow {
            emit(Resource.Loading("Mengecek jenis kartu"))
            try {
                val cardBinRangeData = db.cardListDao().findCardByCardNumber(getCardNo(cardNumber))
                val binRangeType = checkBinRange(cardBinRangeData?.name)

                emit(Resource.Success(binRangeType))
            } catch (e: Exception) {
                LogSdk.error(TAG, e.stackTraceToString())
                if (e is SQLiteException) {
                    emit(Resource.Error("Terjadi kesalahan saat menegecek jenis kartu"))
                } else {
                    emit(Resource.Error("Terjadi kesalahan"))
                }
            }
        }
    }

    override fun classifyCard(binType: BinType): CardClassification {
        return when (binType) {
            BinType.BNI_DEBIT, BinType.OTHER_DEBIT -> CardClassification.DEBIT
            BinType.BNI_CREDIT, BinType.OTHER_CREDIT -> CardClassification.CREDIT
            else -> CardClassification.DEBIT
        }
    }

    private fun checkBinRange(name: String?): BinType {
        if (name.isNullOrBlank()) return BinType.UNKNOWN

        return BinType.entries.firstOrNull {
            it.description.equals(name.trim(), ignoreCase = true)
        } ?: BinType.UNKNOWN
    }

    private fun getCardNo(cardNo: String): Long {
        return cardNo.take(10).toLong()
    }
}