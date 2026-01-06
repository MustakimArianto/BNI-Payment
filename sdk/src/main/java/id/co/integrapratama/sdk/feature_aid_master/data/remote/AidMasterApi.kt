package id.co.integrapratama.sdk.feature_aid_master.data.remote

import id.co.integrapratama.sdk.feature_aid_master.data.dto.AidMasterRequestDto
import id.co.integrapratama.sdk.feature_aid_master.data.dto.AidMasterResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AidMasterApi {
    @POST("getAidMaster")
    suspend fun getAidMaster(@Body request: AidMasterRequestDto): Response<AidMasterResponseDto>
}