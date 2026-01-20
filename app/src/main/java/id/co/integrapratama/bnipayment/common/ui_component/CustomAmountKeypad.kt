package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.ui.theme.PrimaryColor

@Composable
fun CustomAmountKeypad(
    onNumberClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Thin divider at the top
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.White
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(top = 2.dp),
        ) {
            // Left side: Number grid
            Column(
                modifier = Modifier.weight(3f),
            ) {
                // Row 1: 1, 2, 3
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    KeypadButton("1", Modifier.weight(1f), onNumberClick)
                    KeypadButton("2", Modifier.weight(1f), onNumberClick)
                    KeypadButton("3", Modifier.weight(1f), onNumberClick)
                }

                // Row 2: 4, 5, 6
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    ) {
                    KeypadButton("4", Modifier.weight(1f), onNumberClick)
                    KeypadButton("5", Modifier.weight(1f), onNumberClick)
                    KeypadButton("6", Modifier.weight(1f), onNumberClick)
                }

                // Row 3: 7, 8, 9
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    ) {
                    KeypadButton("7", Modifier.weight(1f), onNumberClick)
                    KeypadButton("8", Modifier.weight(1f), onNumberClick)
                    KeypadButton("9", Modifier.weight(1f), onNumberClick)
                }

                // Row 4: 00, 0, 000
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    ) {
                    KeypadButton("00", Modifier.weight(1f), onNumberClick)
                    KeypadButton("0", Modifier.weight(1f), onNumberClick)
                    KeypadButton("000", Modifier.weight(1f), onNumberClick)
                }
            }

            // Right side: Action buttons stacked vertically
            Column(
                modifier = Modifier.weight(1f),
            ) {
                // Backspace button
                KeypadActionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    backgroundColor = Color(0xFFFFC107), // Yellow
                    onClick = onBackspaceClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Backspace",
                        tint = Color.White
                    )
                }

                // Clear button
                KeypadActionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    backgroundColor = Color(0xFFF44336), // Red
                    onClick = onClearClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.White
                    )
                }

                // OK button (takes up 2x height)
                KeypadActionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    backgroundColor = Color(0xFF4CAF50), // Green
                    onClick = onConfirmClick
                ) {
                    Text(
                        text = "OK",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable { onClick(text) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )
    }
}

@Composable
private fun KeypadActionButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}