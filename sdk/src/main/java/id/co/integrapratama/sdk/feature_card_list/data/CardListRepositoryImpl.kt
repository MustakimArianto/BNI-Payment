package id.co.integrapratama.sdk.feature_card_list.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.feature_card_list.data.dto.CardListRequestDto
import id.co.integrapratama.sdk.feature_card_list.data.dto.CardListResponseDto
import id.co.integrapratama.sdk.feature_card_list.data.dto.RequestCardList
import id.co.integrapratama.sdk.feature_card_list.data.dto.toEntity
import id.co.integrapratama.sdk.feature_card_list.data.remote.CardListApi
import id.co.integrapratama.sdk.feature_card_list.domain.CardListRepository
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CardListRepositoryImpl @Inject constructor(
    val api: CardListApi,
    private val db: AppDatabase,
    private val deviceManagerUtility: DeviceManagerUtility
) : CardListRepository {
    companion object {
        const val TAG = "CardListRepositoryImpl"
    }
    val cardListDao = db.cardListDao()

    override fun getCardList(): Flow<Resource<CardListResponseDto>> {
        return flow {
            try {
                emit(Resource.Loading("Mendownload Daftar Kartu.."))

                val request = CardListRequestDto(
                    RequestCardList(
                        sn = deviceManagerUtility.getSerialNumberDevice()
                    )
                )

                val result = api.getCardList(request)

                if (result.isSuccessful) {
                    cardListDao.clearCardList()

                    val data = result.body()!!

                    val cardListData = data.data.map { card ->
                        card.toEntity()
                    }

                    cardListDao.insertAll(cardListData)

                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error(result.message()))
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
}