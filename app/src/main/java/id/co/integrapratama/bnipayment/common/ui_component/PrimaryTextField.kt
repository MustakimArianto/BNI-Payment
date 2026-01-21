package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R


@Composable
fun PrimaryTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(22.dp),
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Done,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.ic_search_gray_24),
                colorFilter = ColorFilter.tint(Color.Black),
                contentDescription = null
            )
        },
        placeholder = {
            Text(stringResource(R.string.placeholder_search_trace_number), fontSize = 14.sp)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFE8F4F8),
            focusedContainerColor = Color(0xFFE8F4F8),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF5C6BC0),
            disabledContainerColor = Color(0xFFE8F4F8)
        ),
        shape = roundedCornerShape,
        modifier = modifier,
        readOnly = readOnly,
        enabled = readOnly.not()
    )
}