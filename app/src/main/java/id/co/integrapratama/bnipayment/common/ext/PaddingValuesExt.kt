package id.co.integrapratama.bnipayment.common.ext

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

fun PaddingValues.copyWith(
    start: Dp = calculateStartPadding(LayoutDirection.Ltr),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(LayoutDirection.Ltr),
    bottom: Dp = calculateBottomPadding()
): PaddingValues {
    return PaddingValues(
        start = start,
        top = top,
        end = end,
        bottom = bottom
    )
}