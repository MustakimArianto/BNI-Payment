package id.co.integrapratama.bnipayment.common.model

import androidx.annotation.DrawableRes

data class MenuItem(
    val title: String,
    @DrawableRes val selectedIcon: Int,
    val onClick: () -> Unit
)