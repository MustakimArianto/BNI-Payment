package id.co.integrapratama.bnipayment.feature_information

import kotlinx.serialization.Serializable

sealed interface InformationRoute {
    @Serializable
    data object Menu : InformationRoute
}