package id.co.integrapratama.bnipayment.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R

val MontserratFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semi_bold, FontWeight.SemiBold),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 57.sp
    ), displayMedium = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 45.sp
    ), displaySmall = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 36.sp
    ), headlineLarge = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 32.sp
    ), headlineMedium = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 28.sp
    ), headlineSmall = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 24.sp
    ), titleLarge = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 22.sp
    ), titleMedium = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp
    ), titleSmall = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp
    ), bodyLarge = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp
    ), bodyMedium = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp
    ), bodySmall = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp
    ), labelLarge = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp
    ), labelMedium = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp
    ), labelSmall = TextStyle(
        fontFamily = MontserratFontFamily, fontWeight = FontWeight.Medium, fontSize = 11.sp
    )
)