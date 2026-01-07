package id.co.integrapratama.bnipayment.common.ui_component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds
import id.co.payment2go.terminalsdkhelper.landi.pinpad.CustomPinPadKeyCode
import kotlin.collections.chunked
import kotlin.collections.forEach
import kotlin.collections.last
import kotlin.collections.map
import kotlin.collections.set
import kotlin.collections.shuffled
import kotlin.collections.take
import kotlin.collections.toMutableList
import kotlin.text.isEmpty
import kotlin.text.isNotBlank

@Composable
fun PinPadButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE0E0E0),
    contentColor: Color = Color.Black
) {
    Box(
        modifier = modifier.background(backgroundColor, shape = RectangleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@Composable
fun CustomPinpad(
    pin: String,
    modifier: Modifier = Modifier,
    isPhysicalKeyboard: Boolean,
    disorder: Boolean = false,
    onUpdatePinpadMapping: (container: CustomPinpadUiBounds, buttons: List<CustomPinpadUiBounds>) -> Unit
) {
    // Collect button + container bounds
    val buttonRects = remember { mutableStateMapOf<String, Rect>() }
    var containerRect by remember { mutableStateOf<Rect?>(null) }
    var hasReported by remember { mutableStateOf(false) }

    // Number List
    var numberList by remember {
        var resultNumberList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
        if (disorder) {
            resultNumberList = resultNumberList.shuffled()
        }
        mutableStateOf(resultNumberList)
    }

    val componentModifier = Modifier
        .graphicsLayer { alpha = if (isPhysicalKeyboard) 0f else 1f }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .then(
                    Modifier.pointerInput(Unit) {
                        detectTapGestures { }
                    }
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White, shape = RectangleShape)
                    .onGloballyPositioned { coords ->
                        containerRect = coords.boundsInWindow()
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // PIN preview
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (pin.isEmpty()) {
                        Text(
                            text = "Please Input PIN",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    } else {
                        repeat(6) { index ->
                            if (index < pin.length) {
                                Text(
                                    text = "●",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Button rows
                val rows = numberList
                    .take(9)
                    .chunked(3)
                    .toMutableList()
                rows.add(listOf("", numberList.last(), ""))
                rows.add(listOf("CANCEL", "CLEAR", "ENTER"))

                var backgroundModifier: Modifier = Modifier
                if (isPhysicalKeyboard) {
                    backgroundModifier = backgroundModifier.background(
                        color = Color(0XFFF2F2F2)
                    )
                }

                Box(
                    modifier = backgroundModifier,
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        rows.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                row.forEach { label ->
                                    PinPadButton(
                                        text = label,
                                        modifier = componentModifier
                                            .weight(1f)
                                            .height(70.dp)
                                            .onGloballyPositioned { coords ->
                                                if (label.isNotBlank()) {
                                                    buttonRects[label] = coords.boundsInWindow()
                                                }
                                            },
                                        backgroundColor = when (label) {
                                            "CANCEL" -> Color(0xFFE53935)
                                            "ENTER" -> Color(0xFF43A047)
                                            "CLEAR" -> Color.Yellow
                                            else -> Color(0XFFF2F2F2)
                                        },
                                        contentColor = if (label in listOf(
                                                "CANCEL",
                                                "ENTER"
                                            )
                                        ) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }

                    if (isPhysicalKeyboard) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.image_physical_pinpad),
                                contentDescription = null,
                                modifier = Modifier.height(height = 100.dp)
                            )
                            Spacer(
                                modifier = Modifier.height(
                                    height = 24.dp
                                )
                            )
                            Text(
                                text = "Please Input PIN With Physical Pinpad",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                                textAlign = TextAlign.Center
                            )
                            Spacer(
                                modifier = Modifier.height(
                                    height = 4.dp
                                )
                            )
                            Text(
                                text = "This device has physical pinpad for inputting pin",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(
                                modifier = Modifier.height(
                                    height = 10.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    // Fire only once when all measurements are ready
    LaunchedEffect(buttonRects.size, containerRect) {
        if (!hasReported && buttonRects.size == 13 && containerRect != null) {
            Log.d(
                "PinPad",
                "📦 Container: " +
                    "x=${containerRect!!.left}, y=${containerRect!!.top}, " +
                    "w=${containerRect!!.width}, h=${containerRect!!.height}"
            )

            buttonRects.forEach { (label, rect) ->
                Log.d(
                    "PinPad",
                    "🔘 $label -> " +
                        "x=${rect.left}, y=${rect.top}, " +
                        "w=${rect.width}, h=${rect.height}"
                )
            }

            val containerUiBounds = CustomPinpadUiBounds(
                label = "Container",
                value = "",
                x = containerRect!!.left,
                y = containerRect!!.top,
                width = containerRect!!.width.toInt(),
                height = containerRect!!.height.toInt()
            )
            val buttonUiBounds = buttonRects.map {
                it.key
                CustomPinpadUiBounds(
                    label = "Container",
                    value = when (it.key) {
                        "1" -> CustomPinPadKeyCode.KEY_1
                        "2" -> CustomPinPadKeyCode.KEY_2
                        "3" -> CustomPinPadKeyCode.KEY_3
                        "4" -> CustomPinPadKeyCode.KEY_4
                        "5" -> CustomPinPadKeyCode.KEY_5
                        "6" -> CustomPinPadKeyCode.KEY_6
                        "7" -> CustomPinPadKeyCode.KEY_7
                        "8" -> CustomPinPadKeyCode.KEY_8
                        "9" -> CustomPinPadKeyCode.KEY_9
                        "0" -> CustomPinPadKeyCode.KEY_0
                        "CANCEL" -> CustomPinPadKeyCode.KEY_CANCEL
                        "ENTER" -> CustomPinPadKeyCode.KEY_ENTER
                        "CLEAR" -> CustomPinPadKeyCode.KEY_CLEAR
                        else -> null
                    },
                    x = it.value.left,
                    y = it.value.top,
                    width = it.value.width.toInt(),
                    height = it.value.height.toInt()
                )
            }
            onUpdatePinpadMapping(containerUiBounds, buttonUiBounds)
            hasReported = true // ✅ ensures callback only once
        }
    }
}