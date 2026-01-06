package id.co.integrapratama.sdk.feature_sale.data

import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.iso8583.IsoConfig
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.feature_sale.domain.SaleRepository
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val isoRepository: Iso8583Repository,
) : SaleRepository {
    companion object {
        private const val TAG = "SaleRepositoryImpl"
        private const val FINANCIAL_REQUEST_MTI = "0200"
        private const val PAYMENT_FROM_SAVING_PROCODE = "501000"
        private const val PAYMENT_FROM_CHECKING_PROCODE = "502000"
    }

    override suspend fun postSaleTransaction(
        isFromSaving: Boolean,
        request: CardReadOutput
    ): Flow<Resource<ByteArray>> {
        return flow {
            emit(Resource.Loading("Harap tunggu"))
            val requestData = mapOf(
                3 to if (isFromSaving) PAYMENT_FROM_SAVING_PROCODE else PAYMENT_FROM_CHECKING_PROCODE,
                4 to request.txnAmount.padAmount(),
                7 to request.txnDate, // Date and Time
                11 to request.STAN,
                12 to request.txnDate, // Time
                13 to request.txnDate, // Date
                18 to "01", // Merchant Type
                22 to request.posEntryMode,
                24 to "041", // NII
                32 to "1234567890123456789012", // Acquiring Institution ID
                35 to request.track2Data,
                41 to "12345678", // TID
                42 to "123456789012345", // MID
                48 to "01551111000000000000000000000000000000000000000000000000000000000000111111111111111111111111111111namenamenamenamenamenamenamenamenamenamenamenamenamenamename11110099999999777777777777777777777777777777777777777777777777777777777777888888888888888888883333333333333333333344444444444444444444666666666666666666", // CSM Data
                52 to request.pinBlock,
                55 to request.emvData
            )
            val response = isoRepository.sendAndReceive(
                isoRepository.createRequest(
                    FINANCIAL_REQUEST_MTI,
                    requestData,
                    IsoConfig.saleRequest
                )
            )

            emit(Resource.Success(byteArrayOf()))
        }
    }
}