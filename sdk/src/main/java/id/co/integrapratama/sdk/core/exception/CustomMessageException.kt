package id.co.integrapratama.sdk.core.exception

class CustomMessageException(
    private val messageValue: String = ""
): Exception() {
    override val message: String?
        get() = messageValue.ifEmpty { null }
}