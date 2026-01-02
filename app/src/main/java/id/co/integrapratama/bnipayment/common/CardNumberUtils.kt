package id.co.integrapratama.bnipayment.common

fun maskCardNumber(cardNumber: String): String {
    if (cardNumber.length <= 10) return cardNumber

    val start = cardNumber.take(6)
    val end = cardNumber.takeLast(4)
    val masked = "*".repeat(cardNumber.length - 10)

    return "$start$masked$end"
}