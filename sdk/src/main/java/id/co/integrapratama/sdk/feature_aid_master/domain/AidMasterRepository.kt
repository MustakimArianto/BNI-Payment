package id.co.integrapratama.sdk.feature_aid_master.domain

import id.co.integrapratama.sdk.feature_aid_master.data.dto.AidMasterResponseDto
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface AidMasterRepository {
    fun getAidMaster(): Flow<Resource<AidMasterResponse>>
}