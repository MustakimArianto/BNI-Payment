package id.co.integrapratama.sdk.core.utils

import id.co.payment2go.terminalsdkhelper.common.Constant

object CardUtil {
    fun panMasking(original: String, pan: String): String {
        if (original.isNotEmpty()) {
            val s = pan.trim()
            val len = s.length
            if (len == 0) return ""

            // For short strings: mask all characters with '*'
            if (len <= 6) return "*".repeat(len)

            val stars = "******"
            val prefixLen = ((len - stars.length) / 2) + 1
            var suffixLen = len - stars.length - prefixLen
            if (suffixLen < 0) {
                suffixLen = 0
            }

            val prefix = s.take(prefixLen)
            val suffix = s.takeLast(suffixLen)
            val masking = prefix + stars + suffix

            return original.replace(pan, masking)
        }
        return ""
    }

    fun getCardMethodFromPosEntryMode(posEntryMode: String): String {
        return when (posEntryMode) {
            Constant.POS_ENTRY_MODE_SWIPE -> "SWIPE"
            Constant.POS_ENTRY_MODE_DIP -> "DIP"
            Constant.POS_ENTRY_MODE_CONTACTLESS -> "TAP"
            else -> ""
        }
    }
}