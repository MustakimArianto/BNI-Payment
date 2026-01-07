package id.co.integrapratama.sdk.feature_bin_range.domain

enum class BinType(val description: String) {
    BNI_DEBIT("Debit BNI"),
    BNI_CREDIT("Credit BNI"),
    OTHER_DEBIT("Debit Bank Lain"),
    OTHER_CREDIT("Credit Bank Lain"),
    UNKNOWN("Kartu Tidak Terdaftar"),
}