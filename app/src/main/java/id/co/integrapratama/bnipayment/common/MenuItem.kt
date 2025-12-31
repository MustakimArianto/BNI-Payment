package id.co.integrapratama.bnipayment.common

import androidx.annotation.DrawableRes

data class MenuItem(
    val title: String,
    @DrawableRes val selectedIcon: Int,
    val destination: String
)