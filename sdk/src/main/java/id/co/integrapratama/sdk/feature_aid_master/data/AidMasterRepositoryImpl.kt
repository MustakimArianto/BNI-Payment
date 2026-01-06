package id.co.integrapratama.sdk.feature_aid_master.data

import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.feature_aid_master.data.dto.AidMasterRequestDto
import id.co.integrapratama.sdk.feature_aid_master.data.dto.RequestAidMaster
import id.co.integrapratama.sdk.feature_aid_master.data.dto.toModel
import id.co.integrapratama.sdk.feature_aid_master.data.local.AidMasterEntity
import id.co.integrapratama.sdk.feature_aid_master.data.remote.AidMasterApi
import id.co.integrapratama.sdk.feature_aid_master.domain.AidMasterRepository
import id.co.integrapratama.sdk.feature_aid_master.domain.AidMasterResponse
import id.co.payment2go.terminalsdkhelper.common.system.device.DeviceManagerUtility
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AidMasterRepositoryImpl(
    private val api: AidMasterApi,
    private val db: AppDatabase,
    private val deviceManagerUtility: DeviceManagerUtility
) : AidMasterRepository {
    val aidDao = db.aidMasterDao()

    override fun getAidMaster(): Flow<Resource<AidMasterResponse>> {
        return flow {
            try {
                emit(Resource.Loading("Getting Aid Master.."))

                val request = AidMasterRequestDto(
                    RequestAidMaster(
                        sn = deviceManagerUtility.getSerialNumberDevice()
                    )
                )

                val result = api.getAidMaster(request)

                if (result.isSuccessful) {
                    aidDao.clearAidMaster()

                    val data = result.body()!!

                    val aidMasterData = data.rows.map { aid ->
                        AidMasterEntity(aids = aid)
                    }

                    aidDao.insertAll(aidMasterData)

                    emit(Resource.Success(data.toModel()))
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