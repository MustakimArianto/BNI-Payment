package id.co.integrapratama.bnipayment.common.ext

fun String.camelCase(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}