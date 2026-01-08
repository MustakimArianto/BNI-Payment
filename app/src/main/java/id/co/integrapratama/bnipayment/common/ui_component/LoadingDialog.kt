package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoadingDialog(title: String = "Memproses", message: String) {
    val spacing = 16.dp

    AlertDialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 16.sp)
            Spacer(Modifier.height(spacing))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(spacing))
            Text(text = "Please Wait", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            if (message.isNotEmpty()) {
                Text(text = message, color = Color(0xFF9E9E9E))
            }
        }
    }
}