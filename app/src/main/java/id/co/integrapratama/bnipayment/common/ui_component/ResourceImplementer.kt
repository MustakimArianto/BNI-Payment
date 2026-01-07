package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import id.co.payment2go.terminalsdkhelper.core.util.Resource

@Composable
fun<T> ResourceImplementer(
    resource: Resource<T>?,
    onLoading: @Composable ((String) -> Unit)? = null,
    onSuccess: @Composable ((T?) -> Unit),
    onError: @Composable ((String) -> Unit)? = null,
    defaultUnknownErrorText: String = "(Unknown Error)"
) {
    if (resource == null) {
        return
    }
    when (resource) {
        is Resource.Loading -> {
            if (onLoading == null) {
                return CircularProgressIndicator()
            }
            onLoading.invoke(resource.message ?: "")
        }
        is Resource.Success -> {
            onSuccess.invoke(resource.data)
        }
        is Resource.Error -> {
            val errorMessage = resource.message ?: defaultUnknownErrorText
            if (onError == null) {
                return Text(text = errorMessage)
            }
            onError.invoke(errorMessage)
        }
    }
}