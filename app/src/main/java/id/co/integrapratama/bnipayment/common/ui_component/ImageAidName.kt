package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.sdk.core.model.AIDName

@Composable
fun ImageAidName(aidName: AIDName) {
    val image = when (aidName) {
        AIDName.GPN -> R.drawable.ic_gpn
        AIDName.MASTERCARD -> R.drawable.ic_mastercard
        AIDName.VISA -> R.drawable.ic_visa
        AIDName.JCB -> R.drawable.ic_jcb
        AIDName.AMERICAN_EXPRESS -> R.drawable.ic_amex
        AIDName.UNKNOWN -> R.drawable.ic_amex
    }

    Image(painter = painterResource(image), contentDescription = null)
}