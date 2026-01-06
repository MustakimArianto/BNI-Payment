package id.co.integrapratama.sdk.core.utils


fun String.padAmount(): String {
    return this.padStart(12, '0')
}