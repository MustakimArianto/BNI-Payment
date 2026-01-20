package id.co.integrapratama.bnipayment.feature_void

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryTextField
import id.co.integrapratama.bnipayment.common.ui_component.TopBar

@Composable
fun VoidInputTraceNoScreen(
    title: String,
    traceNo: String = "",
    onTraceNoChanged: (String) -> Unit,
    onTraceClick: () -> Unit,
) {
    Column {
        TopBar(title = title)
        Column(Modifier.padding(16.dp)) {
            Row {
                PrimaryTextField(
                    value = traceNo,
                    onValueChange = onTraceNoChanged,
                    label = "Trace No"
                )
                Button(onClick = onTraceClick) {
                    Text(stringResource(R.string.text_trace))
                }
            }
        }

    }
}