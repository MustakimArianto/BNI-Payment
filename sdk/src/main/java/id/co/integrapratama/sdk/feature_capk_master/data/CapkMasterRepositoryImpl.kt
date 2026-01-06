package id.co.integrapratama.sdk.feature_capk_master.data

import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.feature_capk_master.data.dto.CapkMasterRequestDto
import id.co.integrapratama.sdk.feature_capk_master.data.dto.CapkMasterResponseDto
import id.co.integrapratama.sdk.feature_capk_master.data.dto.RequestCapkMaster
import id.co.integrapratama.sdk.feature_capk_master.data.dto.toEntity
import id.co.integrapratama.sdk.feature_capk_master.data.remote.CapkMasterApi
import id.co.integrapratama.sdk.feature_capk_master.domain.CapkMasterRepository
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CapkMasterRepositoryImpl(
    private val api: CapkMasterApi,
    private val db: AppDatabase,
    private val deviceManagerUtility: DeviceManagerUtility
) : CapkMasterRepository {
    val capkDao = db.capkMasterDao()

    override fun getCapkMaster(): Flow<Resource<CapkMasterResponseDto>> {
        return flow {
            try {
                emit(Resource.Loading("Getting CAPK Master.."))

                val request = CapkMasterRequestDto(
                    RequestCapkMaster(
                        sn = deviceManagerUtility.getSerialNumberDevice()
                    )
                )

                val result = api.getCapkMaster(request)

                if (result.isSuccessful) {
                    capkDao.clearCapkMaster()

                    val data = result.body()!!

                    val capkMasterData = data.rows.map { capk ->
                        capk.toEntity()
                    }

                    capkDao.insertAll(capkMasterData)

                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error(result.message()))
                }
            } catch (e: Exception) {
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