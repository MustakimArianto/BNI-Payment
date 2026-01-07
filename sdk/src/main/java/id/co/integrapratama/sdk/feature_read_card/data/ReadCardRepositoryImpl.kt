package id.co.integrapratama.sdk.feature_read_card.data

import android.util.Log
import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.StanManager
import id.co.integrapratama.sdk.core.data.local.AppDatabase
import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds
import id.co.integrapratama.sdk.core.utils.AidUtil
import id.co.integrapratama.sdk.feature_capk_master.data.local.toCapkParam
import id.co.integrapratama.sdk.feature_read_card.domain.PinPadEvent
import id.co.integrapratama.sdk.feature_read_card.domain.ReadCardModel
import id.co.integrapratama.sdk.feature_read_card.domain.ReadCardRepository
import id.co.payment2go.terminalsdkhelper.common.SupportCustomPinpad
import id.co.payment2go.terminalsdkhelper.common.emv.AidKernelConfig
import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.common.emv.EMVResponse
import id.co.payment2go.terminalsdkhelper.common.emv.EMVUtility
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.common.pinpad.PinpadUtility
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import id.co.payment2go.terminalsdkhelper.ingenico.emv.VerifyEMVResult
import id.co.payment2go.terminalsdkhelper.landi.LandiBytesUtil
import id.co.payment2go.terminalsdkhelper.landi.pinpad.CustomPinPadButtonLayout
import id.co.payment2go.terminalsdkhelper.landi.pinpad.CustomPinPadKeyCode
import id.co.payment2go.terminalsdkhelper.landi.pinpad.CustomPinpadParameter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.internal.toImmutableList
import javax.inject.Inject

class ReadCardRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val emvUtility: EMVUtility,
    private val pinpadUtility: PinpadUtility,
    private val stanManager: StanManager
) : ReadCardRepository {
    companion object {
        private const val TAG = "ReadCardRepositoryImpl"
    }

    override suspend fun readCard(
        cardOption: CardOption,
    ): Flow<Resource<ReadCardModel>> {
        return callbackFlow {
            emvUtility.stopEMVSearch(true)

            emvUtility.searchCardFirst(
                cardOption = cardOption,
                stan = stanManager.getCurrentStan(),
                emvResponseParam = object : EMVResponse {
                    override fun onCardDetected(type: Int) {
                        Log.d(TAG, "onCardDetected: $type")
                        LogSdk.info(TAG, "onCardDetected: $type")
                        trySend(Resource.Loading(""))
                    }

                    override fun onLoading(message: String) {
                        LogSdk.info(TAG, "onLoading: $message")
                        trySend(Resource.Loading(message))
                    }

                    override fun onCardConfirmation(cardReadOutput: CardReadOutput) {
                        Log.d(TAG, "onCardConfirmation: $cardReadOutput")
                        LogSdk.info(TAG, "onCardConfirmation: $cardReadOutput")
                        val readCardModel = ReadCardModel(cardReadOutput = cardReadOutput)
                        trySend(Resource.Loading("", readCardModel))
                    }

                    override fun onCardHolderVerification(emvCvmMethod: Int) {
                        Log.d(TAG, "onCardHolderVerification: $emvCvmMethod")
                        LogSdk.info(TAG, "onCardHolderVerification: $emvCvmMethod")
                    }

                    override fun onInsertOnlinePin(
                        cardNumber: String,
                        action: OnInsertOnlinePinAction
                    ) {
                        LogSdk.info(TAG, "onInsertOnlinePin: $cardNumber")
                        trySend(
                            Resource.Loading(
                                "",
                                ReadCardModel(
                                    isShowPinpad = true,
                                    cardReadOutput = CardReadOutput(
                                        cardNo = cardNumber,
                                        insertModeCode = action.getCardReadOutput().insertModeCode
                                    ),
                                    onInsertOnlinePinAction = action
                                )
                            )
                        )
                    }

                    override fun processToHost(cardReadOutput: CardReadOutput) {
                        LogSdk.info(TAG, "processToHost: $cardReadOutput")
                        val readCardModel = ReadCardModel(cardReadOutput = cardReadOutput)
                        trySend(Resource.Success(readCardModel))
                    }

                    override fun onError(message: String) {
                        LogSdk.info(TAG, "onError: $message")
                        trySend(Resource.Error(message))
                    }
                }
            )

            awaitClose {
                Log.d(TAG, "Closing callbackFlow, stopping EMV search")
                LogSdk.info(TAG, "Closing callbackFlow, stopping EMV search")
                emvUtility.stopEMVSearch(true)
            }
        }
    }

    override suspend fun injectAids(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading("Menyiapkan AIDs"))
            val aidStringList = appDatabase.aidMasterDao().getAidMasters()
            val aidParamList = AidUtil.parseAidStringListToAidParamList(
                aidStringList = aidStringList.map { it.aids },
                emvUtility = emvUtility
            ).toMutableList()

            fun checkingNSICCSAID(aid: ByteArray?): Boolean {
                val aidHexString = LandiBytesUtil.bytes2HexString(aid ?: byteArrayOf())
                return aidHexString == "D3600000010000" || aidHexString == "A0000006021010"
            }

            val filteredAidParamList = aidParamList.filter {
                checkingNSICCSAID(it.aid)
            }
            filteredAidParamList.forEach {
                val aidParamIndex = aidParamList.indexOfFirst { it2 ->
                    val aidHexString = LandiBytesUtil.bytes2HexString(it.aid ?: byteArrayOf())
                    val aid2HexString = LandiBytesUtil.bytes2HexString(it2.aid ?: byteArrayOf())
                    aidHexString == aid2HexString
                }
                aidParamList[aidParamIndex] = aidParamList[aidParamIndex].copy(
                    kernelConfig = AidKernelConfig(0x63)
                )
            }

            emvUtility.injectAidParams(aidParamList.toImmutableList())
            emit(Resource.Success(Unit))
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error("Terjadi kesalahan saat menyiapkan AIDs"))
        }
    }

    override suspend fun injectCapks(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading("Menyiapkan CAPK"))
            val capkMasterEntityList = appDatabase.capkMasterDao().getCapkMasters()

            emvUtility.injectCapkParams(
                capkMasterEntityList.map {
                    val capkParam = it.toCapkParam()
                    var ridString = "(Empty)"
                    var indexString = "(Empty)"
                    var modString = "(Empty)"
                    var expString = "(Empty)"
                    var expDateString = "(Empty)"
                    var algorithmString = "(Empty)"
                    var hashString = "(Empty)"
                    var hashFlagString = "(Empty)"
                    val ridByteArray = capkParam.rid ?: byteArrayOf()
                    val indexByte = capkParam.index
                    val modByteArray = capkParam.mod ?: byteArrayOf()
                    val expByteArray = capkParam.exp ?: byteArrayOf()
                    val expDateByteArray = capkParam.expDate ?: byteArrayOf()
                    val algorithmInt = capkParam.algorithm
                    val hashByteArray = capkParam.hash ?: byteArrayOf()
                    val hashFlagByte = capkParam.hashFlag

                    if (ridByteArray.isNotEmpty()) {
                        ridString = LandiBytesUtil.bytes2HexString(ridByteArray)
                    }
                    indexString = LandiBytesUtil.bytes2HexString(byteArrayOf(indexByte))
                    if (modByteArray.isNotEmpty()) {
                        modString = LandiBytesUtil.bytes2HexString(modByteArray)
                    }
                    if (expByteArray.isNotEmpty()) {
                        expString = LandiBytesUtil.bytes2HexString(expByteArray)
                    }
                    if (expDateByteArray.isNotEmpty()) {
                        expDateString = LandiBytesUtil.bytes2HexString(expDateByteArray)
                    }
                    algorithmString = algorithmInt.toString()
                    if (hashByteArray.isNotEmpty()) {
                        hashString = LandiBytesUtil.bytes2HexString(hashByteArray)
                    }
                    hashFlagString = LandiBytesUtil.bytes2HexString(byteArrayOf(hashFlagByte))
                    Log.d("CAPK", "---------------------------------")
                    Log.d("CAPK", "RID: $ridString")
                    Log.d("CAPK", "Index: $indexString")
                    Log.d("CAPK", "Mod: $modString")
                    Log.d("CAPK", "Exp: $expString")
                    Log.d("CAPK", "Exp Date: $expDateString")
                    Log.d("CAPK", "Algorithm: $algorithmString")
                    Log.d("CAPK", "Hash: $hashString")
                    Log.d("CAPK", "Hash Flag: $hashFlagString")
                    capkParam
                }
            )

            emit(Resource.Success(Unit))
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error("Terjadi kesalahan saat menyiapkan CAPK"))
        }
    }

    override suspend fun confirmCard(amount: Long): Flow<Resource<Unit>> {
        return flow {
            emvUtility.confirmCardRecord(amount)
            emit(Resource.Success(Unit))
        }
    }

    override suspend fun physicalPinpad(
        cardNumber: String, onInsertOnlinePinAction: OnInsertOnlinePinAction?
    ): Flow<PinPadEvent> {
        return callbackFlow {
            pinpadUtility.showPinPad(
                disorder = false, cardNumber = cardNumber, onPinpadResult = { pinpadResult ->
                    trySend(
                        PinPadEvent(
                            result = pinpadResult, action = onInsertOnlinePinAction
                        )
                    )
                })

            awaitClose { }
        }
    }

    override suspend fun screenPinpad(
        cardNumber: String,
        containerInfo: CustomPinpadUiBounds,
        pinpadMap: List<CustomPinpadUiBounds>,
        onInsertOnlinePinAction: OnInsertOnlinePinAction?,
    ): Flow<PinPadEvent> {
        return callbackFlow {
            if (pinpadUtility is SupportCustomPinpad) {
                val containerInfo = containerInfo
                val pinpadMap = pinpadMap
                pinpadUtility.showCustomPinPad(
                    disorder = false,
                    cardNumber = cardNumber,
                    customPinpadParameter = CustomPinpadParameter(
                        width = containerInfo.width,
                        height = containerInfo.height,
                        x = containerInfo.x.toInt(),
                        y = containerInfo.y.toInt(),
                        pinButtonLayouts = pinpadMap.map {
                            CustomPinPadButtonLayout(
                                width = it.width,
                                height = it.height,
                                x = it.x.toInt(),
                                y = it.y.toInt(),
                                keyCode = it.value as? CustomPinPadKeyCode
                                    ?: CustomPinPadKeyCode.KEY_ENTER
                            )
                        }),
                    onPinpadResult = { pinpadResult ->
                        trySend(
                            PinPadEvent(
                                result = pinpadResult,
                                action = onInsertOnlinePinAction,
                                pinpadState = null
                            )
                        )

                    },
                    onUpdatePinPadState = { pinpadState ->
                        trySend(
                            PinPadEvent(
                                result = null,
                                action = onInsertOnlinePinAction,
                                pinpadState = pinpadState
                            )
                        )
                    })
            } else {
                close(Exception("Custom pinpad not supported"))
            }

            awaitClose { }
        }
    }

    override suspend fun confirmInputPin(
        pin: String, isNonePin: Boolean
    ) {
        emvUtility.confirmPinInput(pin, isNonePin)
    }

    override suspend fun verifyEMVHost(
        emvHost: String?, authCode: String?, arc: String?, authorizeFlag: String?
    ): Flow<Resource<Unit>> {
        return callbackFlow {
            trySend(Resource.Loading("Verifikasi kartu"))
            emvUtility.verifyEMVHost(
                emvHost, authCode, arc, authorizeFlag, onResult = object : VerifyEMVResult {
                    override fun success() {
                        trySend(Resource.Success(Unit))
                    }

                    override fun error(code: Int, message: String) {
                        trySend(Resource.Error(message))
                    }
                })

            awaitClose { }
        }
    }
}