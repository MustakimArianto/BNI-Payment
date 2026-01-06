package id.co.integrapratama.sdk.core.utils

import android.util.Log
import id.co.payment2go.terminalsdkhelper.common.emv.AidKernelConfig
import id.co.payment2go.terminalsdkhelper.common.emv.AidParam
import id.co.payment2go.terminalsdkhelper.common.emv.AmexAidKernelConfig
import id.co.payment2go.terminalsdkhelper.common.emv.DiscoverAidKernelConfig
import id.co.payment2go.terminalsdkhelper.common.emv.EMVUtility
import id.co.payment2go.terminalsdkhelper.common.emv.JcbAidKernelConfig
import id.co.payment2go.terminalsdkhelper.common.emv.MasterAidKernelConfig
import id.co.payment2go.terminalsdkhelper.common.emv.PbocAidKernelConfig
import id.co.payment2go.terminalsdkhelper.common.emv.VisaAidKernelConfig
import id.co.payment2go.terminalsdkhelper.common.emv.aidpriority.toAidPriority
import id.co.payment2go.terminalsdkhelper.core.util.Util
import id.co.payment2go.terminalsdkhelper.landi.LandiBytesUtil

object AidUtil {
    fun parseAidStringListToAidParamList(
        aidStringList: List<String>,
        emvUtility: EMVUtility
    ): List<AidParam> {
        val aidParamList: MutableList<AidParam> = mutableListOf()
        for (aidString in aidStringList.withIndex()) {
            aidParamList.add(
                parseAidString(
                    aidString = aidString.value,
                    emvUtility = emvUtility
                )
            )
        }
        return aidParamList.toList()
    }

    fun parseAidString(
        aidString: String,
        emvUtility: EMVUtility
    ) : AidParam {
        val getSupportTLVParsingForEMV = Util.getSupportTLVParsing(emvUtility)
        val tlvResultMap: MutableMap<String, ByteArray> = mutableMapOf()
        // The tag list specification is based on BIT (Bringin Inti Teknologi) BRI
        val tagPairList = listOf(
            Pair("9F06", "AID"),
            Pair("9F09", "AID Version"),
            Pair("DF11", "TAC Default"),
            Pair("DF13", "TAC Denial"),
            Pair("DF12", "TAC Online"),
            Pair("DF15", "Threshold (lmt)"),
            Pair("DF17", "Target Percentage"),
            Pair("DF16", "Max Target Percentage"),
            Pair("DF14", "DDOL (Dynamic Data Authentication Data Object List)"),
            Pair("97", "TDOL (Transaction Certificate Data Object List)"),
            Pair("9F1B", "Floor Limit"),
            Pair("DF01", "APP Select"),
            Pair("87", "AID Priority"),
            Pair("9C", "Transaction Type 9C"),
            Pair("9F15", "Category Code"),
            Pair("9F1A", "Terminal Country Code"),
            Pair("5F2A", "Transaction Currency Code"),
            Pair("DF19", "Contactless Floor Limit"),
            Pair("DF20", "Contactless Transaction Limit"),
            Pair("DF21", "Contactless CVM Limit"),
            Pair("9F33", "Terminal Capability"),
            Pair("9F40", "Additional Terminal Capability"),
        )
        val aidStringBuilder: StringBuilder = StringBuilder(aidString)
        Log.d("rawTLV", "---------------------------------")
        Log.d("rawTLV", aidString)
        for (tagPair in tagPairList) {
            val tag = tagPair.first
            val tagDescription = tagPair.second
            val byteArray = getSupportTLVParsingForEMV?.getTagFromTLVString(
                tag = tag,
                tlvString = aidStringBuilder.toString()
            )
            tlvResultMap[tag] = byteArray ?: byteArrayOf()
            // Remove the back of tag
            val index = aidStringBuilder.indexOf(tag)
            if (index != -1) {
                aidStringBuilder.delete(0, index)
            }
            val hexString = LandiBytesUtil.bytes2HexString(tlvResultMap[tag]!!)
            // Remove founded TLV string based tag
            var length = tag.length + 2 + hexString.length
            if (length > aidStringBuilder.length) {
                length = aidStringBuilder.length
            }
            aidStringBuilder.delete(0, length)
            Log.d("TLV", "$tag - $tagDescription = $hexString (${tlvResultMap[tag]!!.asList()})")
        }
        val aidString = LandiBytesUtil.bytes2HexString(tlvResultMap["9F06"] ?: byteArrayOf())
        val df01ByteList = (tlvResultMap["DF01"]?.asList() ?: listOf())
        var partSelect: Byte = 0
        if (df01ByteList.isNotEmpty()) {
            partSelect = df01ByteList.first()
        }
        val transactionTypeByteList = (tlvResultMap["9C"]?.asList() ?: listOf())
        var transactionType: Byte = 0
        if (transactionTypeByteList.isNotEmpty()) {
            transactionType = transactionTypeByteList.first()
        }
        var aidKernelConfig: AidKernelConfig? = null
        if (aidString.contains("A000000003")) {
            aidKernelConfig = VisaAidKernelConfig()
        } else if (aidString.contains("A000000004")) {
            aidKernelConfig = MasterAidKernelConfig()
        } else if (aidString.contains("A000000333")) {
            aidKernelConfig = PbocAidKernelConfig()
        } else if (aidString.contains("A000000025") || aidString.contains("A000000025")) {
            aidKernelConfig = AmexAidKernelConfig()
        } else if (aidString.contains("A000000065")) {
            aidKernelConfig = JcbAidKernelConfig()
        } else if (aidString.contains("A000000152")) {
            aidKernelConfig = DiscoverAidKernelConfig()
        }
        val priorityByteList = (tlvResultMap["87"]?.asList() ?: listOf())
        var priority: Byte = 0
        if (priorityByteList.isNotEmpty()) {
            priority = priorityByteList.first()
        }
        return AidParam(
            aid = tlvResultMap["9F06"],
            partSelect = partSelect,
            ddol = tlvResultMap["DF14"],
            tdol = tlvResultMap["97"],
            tmCap = tlvResultMap["9F33"],
            tmCapAd = tlvResultMap["9F40"],
            appVerNo = tlvResultMap["9F09"],
            lmt = tlvResultMap["DF15"],
            percent = tlvResultMap["DF17"],
            maxPercent = tlvResultMap["DF16"],
            tacDefault = tlvResultMap["DF11"],
            tacDenial = tlvResultMap["DF13"],
            tacOnline = tlvResultMap["DF12"],
            floorLmt = tlvResultMap["9F1B"],
            aidType = 0,
            tmType = transactionType,
            rfLimt = tlvResultMap["DF20"],
            rfCVMLimt = tlvResultMap["DF21"],
            rfFloorLmt = tlvResultMap["DF19"],
            kernelConfig = aidKernelConfig,
            priority = priority.toInt().toAidPriority()
        )
    }
}