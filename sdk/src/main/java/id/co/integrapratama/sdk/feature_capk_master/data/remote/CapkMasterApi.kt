package id.co.integrapratama.sdk.feature_capk_master.data.remote

import id.co.integrapratama.sdk.feature_capk_master.data.dto.CapkMasterRequestDto
import id.co.integrapratama.sdk.feature_capk_master.data.dto.CapkMasterResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CapkMasterApi {
    @POST("getCapkMaster")
    suspend fun getCapkMaster(@Body request: CapkMasterRequestDto): Response<CapkMasterResponseDto>
}