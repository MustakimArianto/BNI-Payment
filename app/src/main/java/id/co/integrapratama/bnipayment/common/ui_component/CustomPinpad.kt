package id.co.integrapratama.bnipayment.common.ui_component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds
import id.co.payment2go.terminalsdkhelper.landi.pinpad.CustomPinPadKeyCode
import java.util.Locale

@Composable
fun PinPadButton(
    text: String,
    fontSize: TextUnit = 32.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFFFFFFF),
    contentColor: Color = Color.Black
) {
    Box(
        modifier = modifier
            .background(backgroundColor, shape = RectangleShape)
            .border(
                width = 0.5.dp,
                color = Color(0xFFCCCCCC)
            )
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = contentColor
        )
    }
}

sealed class CustomPinpadType {
    class FullPinpadWithBackground(
        val pin: String,
        val onUpdatePinpadMapping: ((container: CustomPinpadUiBounds, buttons: List<CustomPinpadUiBounds>) -> Unit)? = null,
    ) : CustomPinpadType()

    class OnlyPinpadButton(
        val behavior: OnlyPinpadButtonBehavior
    ) : CustomPinpadType()
}

sealed class OnlyPinpadButtonBehavior {
    class OnPinpadInputPerCharacterBehavior(
        val onPinpadInputPerCharacter: ((CustomPinPadKeyCode?, String) -> Unit)? = null
    ): OnlyPinpadButtonBehavior()

    class OnPinpadInputBasedEventBehavior(
        val pinLength: Int = 4,
        val onEvent: ((PinpadInputEvent) -> Unit)? = null
    ): OnlyPinpadButtonBehavior()
}

sealed class PinpadInputEvent {
    data class InputPin(val pin: String): PinpadInputEvent()
    data class ClearPin(val pin: String): PinpadInputEvent()
    data class CancelPin(val behavior: PinpadInputEventCancelPinBehavior): PinpadInputEvent()
    data class EnterPin(val pin: String): PinpadInputEvent()
}

sealed class PinpadInputEventCancelPinBehavior {
    data class FirstCancelPinBehavior(val pin: String): PinpadInputEventCancelPinBehavior()
    object SecondCancelPinBehavior: PinpadInputEventCancelPinBehavior()
}

private fun convertFromKeyToCustomPinPadKeyCode(key: String): CustomPinPadKeyCode? {
    return when (key) {
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
    }
}

@Composable
fun CustomPinpad(
    isPhysicalKeyboard: Boolean,
    disorder: Boolean = false,
    type: CustomPinpadType
) {
    when (type) {
        is CustomPinpadType.FullPinpadWithBackground -> {
            CustomPinpad(
                pin = type.pin,
                isPhysicalKeyboard = isPhysicalKeyboard,
                disorder = disorder,
                onUpdatePinpadMapping = type.onUpdatePinpadMapping,
                usingDialog = true,
                isFull = true
            )
        }
        is CustomPinpadType.OnlyPinpadButton -> {
            var currentPin by remember { mutableStateOf("") }
            CustomPinpad(
                pin = "",
                isPhysicalKeyboard = isPhysicalKeyboard,
                disorder = disorder,
                onPinpadInput = { keyCode, value ->
                    val behavior = type.behavior
                    when (keyCode) {
                        CustomPinPadKeyCode.KEY_ENTER -> {
                            if (behavior is OnlyPinpadButtonBehavior.OnPinpadInputBasedEventBehavior) {
                                if (currentPin.length == behavior.pinLength) {
                                    behavior.onEvent?.invoke(PinpadInputEvent.EnterPin(currentPin))
                                }
                            }
                        }
                        CustomPinPadKeyCode.KEY_CLEAR -> {
                            if (behavior is OnlyPinpadButtonBehavior.OnPinpadInputBasedEventBehavior) {
                                val newPinStringBuilder = StringBuilder(currentPin)
                                if (newPinStringBuilder.isNotEmpty()) {
                                    newPinStringBuilder.setLength(newPinStringBuilder.length - 1)
                                    currentPin = newPinStringBuilder.toString()
                                }
                                behavior.onEvent?.invoke(PinpadInputEvent.ClearPin(currentPin))
                            }
                        }
                        CustomPinPadKeyCode.KEY_CANCEL -> {
                            if (behavior is OnlyPinpadButtonBehavior.OnPinpadInputBasedEventBehavior) {
                                lateinit var cancelPinBehavior: PinpadInputEventCancelPinBehavior
                                if (currentPin.isNotEmpty()) {
                                    val newPinStringBuilder = StringBuilder(currentPin)
                                    newPinStringBuilder.setLength(0)
                                    currentPin = newPinStringBuilder.toString()
                                    cancelPinBehavior = PinpadInputEventCancelPinBehavior.FirstCancelPinBehavior(currentPin)
                                } else {
                                    cancelPinBehavior = PinpadInputEventCancelPinBehavior.SecondCancelPinBehavior
                                }
                                behavior.onEvent?.invoke(PinpadInputEvent.CancelPin(cancelPinBehavior))
                            }
                        }
                        else -> {
                            if (value.isDigitsOnly()) {
                                if (behavior is OnlyPinpadButtonBehavior.OnPinpadInputBasedEventBehavior) {
                                    val newPinStringBuilder = StringBuilder(currentPin)
                                    newPinStringBuilder.append(value)
                                    if (newPinStringBuilder.length > behavior.pinLength) {
                                        newPinStringBuilder.setLength(behavior.pinLength)
                                    }
                                    currentPin = newPinStringBuilder.toString()
                                    behavior.onEvent?.invoke(PinpadInputEvent.InputPin(currentPin))
                                }
                            }
                        }
                    }
                    if (behavior is OnlyPinpadButtonBehavior.OnPinpadInputPerCharacterBehavior) {
                        behavior.onPinpadInputPerCharacter?.invoke(keyCode, value)
                    }
                },
                usingDialog = false,
                isFull = false
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun CustomPinpad(
    pin: String,
    isPhysicalKeyboard: Boolean,
    disorder: Boolean = false,
    usingDialog: Boolean = true,
    isFull: Boolean = true,
    onUpdatePinpadMapping: ((container: CustomPinpadUiBounds, buttons: List<CustomPinpadUiBounds>) -> Unit)? = null,
    onPinpadInput: ((CustomPinPadKeyCode?, String) -> Unit)? = null,
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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val pinpadHeight = 52.dp

    @Composable
    fun pinpadContent() {
        var boxModifier: Modifier = Modifier
        if (isFull) {
            boxModifier = boxModifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .then(
                    Modifier.pointerInput(Unit) {
                        detectTapGestures { }
                    }
                )
        }
        Box(
            modifier = boxModifier,
            contentAlignment = if (isFull) Alignment.BottomCenter else Alignment.TopStart
        ) {
            var columnModifier: Modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, shape = RectangleShape)
            if (isFull) {
                columnModifier = columnModifier.onGloballyPositioned { coords ->
                    containerRect = coords.boundsInWindow()
                }
            }
            Column(
                modifier = columnModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // PIN preview
                if (isFull) {
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
                }

                // Button rows
                val rows = numberList
                    .take(9)
                    .chunked(3)
                    .toMutableList()

                var backgroundModifier: Modifier = Modifier
                if (isPhysicalKeyboard) {
                    backgroundModifier = backgroundModifier.background(
                        color = Color(0XFFFFFFFF)
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
                        Box(
                            modifier = Modifier.height(
                                height = 0.5.dp,
                            ).background(
                                color = Color(0xFFCCCCCC)
                            ).fillMaxWidth()
                        )
                        @Composable
                        fun pinpadButton(
                            label: String,
                            height: Dp,
                        ) {
                            var pinpadButtonModifier: Modifier = componentModifier
                                .width(screenWidth / 3)
                                .height(height)
                            if (isFull) {
                                pinpadButtonModifier = pinpadButtonModifier.onGloballyPositioned { coords ->
                                    if (label.isNotBlank()) {
                                        buttonRects[label] = coords.boundsInWindow()
                                    }
                                }
                            } else {
                                pinpadButtonModifier = pinpadButtonModifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onPinpadInput?.invoke(
                                        convertFromKeyToCustomPinPadKeyCode(label), label
                                    )
                                }
                            }
                            PinPadButton(
                                text = label.lowercase(
                                    Locale.getDefault()
                                ).replaceFirstChar {
                                    if (it.isLowerCase()) {
                                        it.titlecase(Locale.getDefault())
                                    } else {
                                        it.toString()
                                    }
                                }
                                ,
                                fontSize = when (label) {
                                    "CANCEL", "ENTER", "CLEAR" -> 20.sp
                                    else -> 32.sp
                                },
                                fontWeight = when (label) {
                                    "CANCEL", "ENTER", "CLEAR" -> FontWeight.Normal
                                    else -> FontWeight.Medium
                                },
                                modifier = pinpadButtonModifier,
                                backgroundColor = when (label) {
                                    "CANCEL" -> Color(0xFFE61919)
                                    "ENTER" -> Color(0xFFFAD514)
                                    "CLEAR" -> Color(0xFF5FC254)
                                    else -> Color(0XFFFFFFFF)
                                },
                                contentColor = if (label in listOf("CANCEL", "CLEAR", "ENTER")) {
                                    Color.White
                                } else {
                                    Color(0xFF004558)
                                }
                            )
                        }
                        rows.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                row.forEach { label ->
                                    pinpadButton(
                                        height = pinpadHeight,
                                        label = label
                                    )
                                }
                            }
                        }
                        Row {
                            pinpadButton(
                                height = pinpadHeight * 2,
                                label = "CANCEL"
                            )
                            Column {
                                pinpadButton(
                                    height = pinpadHeight,
                                    label = numberList.last()
                                )
                                pinpadButton(
                                    height = pinpadHeight,
                                    label = "CLEAR"
                                )
                            }
                            pinpadButton(
                                height = pinpadHeight * 2,
                                label = "ENTER"
                            )
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

    if (usingDialog) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            pinpadContent()
        }
    } else {
        pinpadContent()
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
                    value = convertFromKeyToCustomPinPadKeyCode(it.key),
                    x = it.value.left,
                    y = it.value.top,
                    width = it.value.width.toInt(),
                    height = it.value.height.toInt()
                )
            }
            onUpdatePinpadMapping?.invoke(containerUiBounds, buttonUiBounds)
            hasReported = true // ✅ ensures callback only once
        }
    }
}