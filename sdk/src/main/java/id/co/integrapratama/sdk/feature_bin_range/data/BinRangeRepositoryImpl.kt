package id.co.integrapratama.sdk.feature_bin_range.data

import android.database.sqlite.SQLiteException
import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.utils.toResourceError
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

                if (binRangeType == BinType.UNKNOWN) {
                    emit(Resource.Error("Jenis kartu tidak terdaftar"))
                } else {
                    emit(Resource.Success(binRangeType))
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, e.stackTraceToString())
                emit(e.toResourceError())
            }
        }
    }

    override fun classifyCard(binType: BinType): CardClassification {
        return when (binType) {
            BinType.DEBIT_SILVER,
            BinType.DEBIT_GOLD,
            BinType.DEBIT_GOLD_2,
            BinType.DEBIT_PLATINUM,
            BinType.DEBIT_PLATINUM_2,
            BinType.DEBIT_EMERALD,
            BinType.DEBIT_CITILINK,
            BinType.DEBIT_KARTU_PEGAWAI,
            BinType.DEBIT_LAFAYETTE,
            BinType.DEBIT_SME,
            BinType.DEBIT_TAPLUS_MUDA,
            BinType.DEBIT_SYARIAH_GOLD,
            BinType.DEBIT_SYARIAH_PLATINUM,
            BinType.DEBIT_SYARIAH_SILVER,
            BinType.PRIVATE_LABEL_SYARIAH,
            BinType.PRIVATE_LABEL_DEBIT,
            BinType.PL_VIRTUAL_ACCOUNT,
            BinType.PL_NPG,
            BinType.PL_KARTU_TANI,
            BinType.PL_KARTU_INDONESIA,
            BinType.PL_KARTU_BANTUAN,
            BinType.PL_VA_BNDRA_PEL,
            BinType.MAESTRO_BNI,
                -> {
                CardClassification.DEBIT
            }

            BinType.CREDIT_GOLD, BinType.CREDIT_OTHER -> {
                CardClassification.CREDIT
            }

            else -> CardClassification.UNKNOWN
        }
    }

    private fun checkBinRange(name: String?): BinType {
        if (name.isNullOrBlank()) return BinType.UNKNOWN

        return BinType.entries.firstOrNull {
            it.description.lowercase().equals(name.lowercase().trim(), ignoreCase = true)
        } ?: BinType.UNKNOWN
    }

    private fun getCardNo(cardNo: String): Long {
        return cardNo.take(10).toLong()
    }
}