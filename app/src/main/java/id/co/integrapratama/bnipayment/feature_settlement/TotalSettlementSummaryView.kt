package id.co.integrapratama.bnipayment.feature_settlement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.common.ui_component.ResourceImplementer
import id.co.integrapratama.sdk.core.utils.StringUtil
import id.co.integrapratama.sdk.feature_settlement.domain.TotalSettlementSummaryModel
import id.co.payment2go.terminalsdkhelper.core.util.Resource

@Composable
fun TotalSettlementSummaryView(
    modifier: Modifier = Modifier,
    totalSettlementSummaryModelResult: Resource<TotalSettlementSummaryModel>
) {
    Box(
        modifier = modifier
    ) {
        ResourceImplementer(
            resource = totalSettlementSummaryModelResult,
            onSuccess = { totalSettlementSummaryModel ->
                Column {
                    Text(
                        fontWeight = FontWeight(584),
                        text = "Total Settlement"
                    )
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Row {
                        Text(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            text = "Sale"
                        )
                        Text(
                            StringUtil.formatRupiahCurrency(
                                totalSettlementSummaryModel!!.totalSale.toString()
                            ),
                            fontWeight = FontWeight(584)
                        )
                    }
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Row {
                        Text(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            text = "Void"
                        )
                        Text(
                            "- ${StringUtil.formatRupiahCurrency(
                                totalSettlementSummaryModel!!.totalVoid.toString()
                            )}",
                            fontWeight = FontWeight(584)
                        )
                    }
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Row {
                        Text(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            text = "Refund"
                        )
                        Text(
                            "- ${StringUtil.formatRupiahCurrency(
                                totalSettlementSummaryModel!!.totalRefund.toString()
                            )}",
                            fontWeight = FontWeight(584)
                        )
                    }
                }
            }
        )
    }
}