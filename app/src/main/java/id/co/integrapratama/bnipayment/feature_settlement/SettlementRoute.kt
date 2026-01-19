package id.co.integrapratama.bnipayment.feature_settlement

import kotlinx.serialization.Serializable

sealed interface SettlementRoute {
    @Serializable
    data object InputMerchantPin : SettlementRoute

    @Serializable
    data object TotalSettlement : SettlementRoute
}
