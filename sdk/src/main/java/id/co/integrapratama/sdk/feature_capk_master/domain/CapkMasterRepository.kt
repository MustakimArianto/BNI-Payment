package id.co.integrapratama.sdk.feature_capk_master.domain

import id.co.integrapratama.sdk.feature_capk_master.data.dto.CapkMasterResponseDto
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface CapkMasterRepository {
    fun getCapkMaster(): Flow<Resource<CapkMasterResponseDto>>
}