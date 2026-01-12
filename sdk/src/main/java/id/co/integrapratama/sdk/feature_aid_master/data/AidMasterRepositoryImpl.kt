package id.co.integrapratama.sdk.feature_aid_master.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.utils.toResourceError
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

class AidMasterRepositoryImpl(
    private val api: AidMasterApi,
    private val db: AppDatabase,
    private val deviceManagerUtility: DeviceManagerUtility
) : AidMasterRepository {
    companion object {
        const val TAG = "AidMasterRepositoryImpl"
    }
    val aidDao = db.aidMasterDao()

    override fun getAidMaster(): Flow<Resource<AidMasterResponse>> {
        return flow {
            try {
                emit(Resource.Loading("Mendownload Data Aid.."))

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
                LogSdk.error(TAG, e.stackTraceToString())
                emit(e.toResourceError())
            }
        }
    }
}