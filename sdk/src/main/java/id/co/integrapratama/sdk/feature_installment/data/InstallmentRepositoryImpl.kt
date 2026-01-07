package id.co.integrapratama.sdk.feature_installment.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.iso8583.IsoConfig
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPeriodModel
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentPlanModel
import id.co.integrapratama.sdk.feature_installment.domain.InstallmentRepository
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class InstallmentRepositoryImpl @Inject constructor(
    private val isoRepository: Iso8583Repository,
) : InstallmentRepository {
    companion object {
        private const val TAG = "InstallmentRepositoryImpl"
        private const val FINANCIAL_REQUEST_MTI = "0200"
        private const val PAYMENT_FROM_SAVING_PROCODE = "501000"
        private const val PAYMENT_FROM_CHECKING_PROCODE = "502000"
    }

    override suspend fun getInstallmentPeriodList(): Flow<Resource<List<InstallmentPeriodModel>>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        listOf(
                            InstallmentPeriodModel(
                                id = "1",
                                name = "3 Bulan"
                            ),
                            InstallmentPeriodModel(
                                id = "2",
                                name = "6 Bulan"
                            ),
                            InstallmentPeriodModel(
                                id = "3",
                                name = "12 Bulan"
                            ),
                            InstallmentPeriodModel(
                                id = "4",
                                name = "18 Bulan"
                            ),
                            InstallmentPeriodModel(
                                id = "5",
                                name = "24 Bulan"
                            )
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> emit(Resource.Error("Tidak ada koneksi Internet"))
                    is ConnectException -> emit(Resource.Error("Tidak dapat terhubung ke server"))
                    is SocketTimeoutException -> emit(Resource.Error("Koneksi Timeout"))
                    else -> emit(Resource.Error(e.message ?: "Unknown error occurred"))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getInstallmentPlanList(): Flow<Resource<List<InstallmentPlanModel>>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        listOf(
                            InstallmentPlanModel(
                                id = "1",
                                name = "Plan 1"
                            ),
                            InstallmentPlanModel(
                                id = "2",
                                name = "Plan 2"
                            ),
                            InstallmentPlanModel(
                                id = "3",
                                name = "Plan 3"
                            ),
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> emit(Resource.Error("Tidak ada koneksi Internet"))
                    is ConnectException -> emit(Resource.Error("Tidak dapat terhubung ke server"))
                    is SocketTimeoutException -> emit(Resource.Error("Koneksi Timeout"))
                    else -> emit(Resource.Error(e.message ?: "Unknown error occurred"))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun postInstallmentTransaction(
        isFromSaving: Boolean,
        request: CardReadOutput,
        transactionDateTime: String
    ): Flow<Resource<ByteArray>> {
        return flow {
            try {
                emit(Resource.Loading("Harap tunggu"))
                val requestData = mapOf(
                    3 to if (isFromSaving) PAYMENT_FROM_SAVING_PROCODE else PAYMENT_FROM_CHECKING_PROCODE,
                    4 to request.txnAmount.padAmount(),
                    7 to transactionDateTime,
                    11 to request.STAN.padStart(6, '0'),
                    12 to DateUtils.getTransactionTime(transactionDateTime), // Time
                    13 to DateUtils.getTransactionDate(transactionDateTime), // Date
                    18 to "0125", // Merchant Type
                    22 to request.posEntryMode + "1",
                    24 to "041", // NII
                    32 to "1234567890123456789012", // Acquiring Institution ID
                    35 to request.track2Data,
                    41 to "12345678", // TID
                    42 to "123456789012345", // MID
                    48 to "01551111000000000000000000000000000000000000000000000000000000000000111111111111111111111111111111namenamenamenamenamenamenamenamenamenamenamenamenamenamename11110099999999777777777777777777777777777777777777777777777777777777777777888888888888888888883333333333333333333344444444444444444444666666666666666666", // CSM Data
                    52 to request.pinBlock,
                    55 to request.emvData
                )
                val packedData = isoRepository.createRequest(
                    FINANCIAL_REQUEST_MTI,
                    requestData,
                    IsoConfig.saleRequest
                )

                if (packedData.isEmpty()) {
                    emit(Resource.Error("Error saat membuat request installment"))
                    return@flow
                }

                isoRepository.sendAndReceive(packedData).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            emit(Resource.Loading("Mengirim request installment...", null))
                        }

                        is Resource.Success -> {
                            emit(Resource.Success(response.data ?: byteArrayOf()))
                        }

                        is Resource.Error -> {
                            emit(Resource.Error(response.message ?: "Terjadi kesalahan"))
                        }
                    }
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