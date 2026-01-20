package id.co.integrapratama.sdk.feature_mini_atm.balance_info.data

import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.TerminalBatchManager
import id.co.integrapratama.sdk.core.TerminalConfigManager
import id.co.integrapratama.sdk.core.TraceNumberManager
import id.co.integrapratama.sdk.core.iso8583.Iso8583Repository
import id.co.integrapratama.sdk.core.model.MTI
import id.co.integrapratama.sdk.core.utils.CardUtil
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.StringUtil
import id.co.integrapratama.sdk.core.utils.padAmount
import id.co.integrapratama.sdk.core.utils.toResourceError
import id.co.integrapratama.sdk.feature_mini_atm.balance_info.core.MiniATMBalanceInfoPrintTemplateFactory
import id.co.integrapratama.sdk.feature_mini_atm.balance_info.domain.MiniATMBalanceInfoRepository
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateParameter
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import id.co.payment2go.terminalsdkhelper.core.util.Util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class MiniATMBalanceInfoRepositoryImpl @Inject constructor(
    private val iso8583Repository: Iso8583Repository,
    private val traceNumberManager: TraceNumberManager,
    private val terminalConfigManager: TerminalConfigManager,
    private val printRepository: PrintRepository,
    private val terminalBatchManager: TerminalBatchManager,
) : MiniATMBalanceInfoRepository {
    companion object {
        private const val TAG = "MiniATMBalanceInfoRepositoryImpl"
        private const val BALANCE_INQUIRY_SAVING_PROCODE = "311000"
        private const val BALANCE_INQUIRY_CHECKING_PROCODE = "312000"
    }

    override suspend fun postBalanceInfoTransaction(
        isFromSaving: Boolean,
        request: CardReadOutput,
        transactionDateTime: Date
    ): Flow<Resource<ByteArray>> {
        return flow {
            try {
                emit(Resource.Loading("Mengirim data transaksi"))

                val processingCode =
                    if (isFromSaving) BALANCE_INQUIRY_SAVING_PROCODE else BALANCE_INQUIRY_CHECKING_PROCODE
                val requestData = mutableMapOf<Int, String>().apply {
                    put(3, processingCode)
                    put(4, request.txnAmount.padAmount())
                    put(7, DateUtils.getTransactionDateTime(transactionDateTime))
                    put(11, request.STAN.padStart(6, '0'))
                    put(12, DateUtils.getTransactionTime(transactionDateTime))
                    put(13, DateUtils.getTransactionDate(transactionDateTime))
                    put(22, request.posEntryMode + "1")
                    put(24, "041")
                    put(25, "00")
                    put(35, request.track2Data.dropLast(1))
                    put(41, terminalConfigManager.getTid() ?: "")
                    put(42, terminalConfigManager.getMid() ?: "")
                    if (request.pinBlock.isNotEmpty()) {
                        put(52, request.pinBlock)
                    }
                    if (request.emvData.isNotEmpty()) {
                        put(55, request.emvData)
                    }
                }

                val packedData = iso8583Repository.createRequest(
                    MTI.AUTHORIZATION.code,
                    requestData,
                )

                if (packedData.isEmpty()) {
                    emit(Resource.Error("Error saat membuat request balance info"))
                    return@flow
                }

                iso8583Repository.sendAndReceive(packedData).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            emit(Resource.Loading("Mengirim request balance info..."))
                        }

                        is Resource.Success -> {
                            traceNumberManager.increment()
                            emit(Resource.Success(resource.data ?: byteArrayOf()))
                        }

                        is Resource.Error -> {
                            emit(Resource.Error(resource.message ?: "Terjadi kesalahan"))
                        }
                    }
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, "postBalanceInfoTransaction: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }

    override fun preparePrintingData(
        balance: String,
        request: CardReadOutput,
        transactionDateTime: Date
    ): PrintBasedOnTemplateParameter {
        val authCode = "711162"
        val refNo = "000047111620000"

        val currentTraceNoText = Util.addZerosToNumber(
            traceNumberManager.getCurrentLastTraceNo(),
            desiredDigits = 6
        )
        val currentBatchNoText = Util.addZerosToNumber(
            terminalBatchManager.getCurrentBatch(),
            desiredDigits = 6
        )

        val printFactory = MiniATMBalanceInfoPrintTemplateFactory(
            branchName = "DUMMY TRX",
            branchAddress = "JL. JENDRAL SUDIRMAN",
            branchCity = "JAKARTA",
            terminalId = "1234567890",
            merchantId = "1234567890",
            cardType = request.cardAppName,
            exp = request.cardExpiry,
            cardNumber = request.cardNo,
            cardMethod = CardUtil.getCardMethodFromPosEntryMode(request.posEntryMode),
            date = DateUtils.getReceiptTransactionDate(transactionDateTime),
            time = DateUtils.getReceiptTransactionTime(transactionDateTime),
            batch = currentBatchNoText,
            trace = currentTraceNoText,
            ref = refNo,
            appr = authCode,
            balance = StringUtil.formatRupiahCurrency(balance),
            version = "V2019.1.0.0.8",
        )

        val printTemplate = printFactory.getPrintBasedOnTemplateParameter { }

        return printTemplate
    }

    override suspend fun printCurrentReceipt(template: PrintBasedOnTemplateParameter): Flow<Resource<Unit>> {
        return flow {
            try {
                emit(Resource.Loading("Mencetak struk"))
                printRepository.printWithBuilder(
                    builder = PrintBasedOnTemplateParameterBuilder().fromJson(
                        valueComponentJsonString = template.jsonString,
                        templateComponentJsonString = template.printTemplateJsonString
                    )
                ).collect {
                    if (it is Resource.Loading) {
                        return@collect
                    }
                    emit(it)
                }
            } catch (e: Exception) {
                LogSdk.error(TAG, "printCurrentReceipt: ${e.stackTraceToString()}")
                emit(e.toResourceError())
            }
        }
    }
}