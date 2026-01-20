package id.co.integrapratama.sdk.feature_settlement.core

import id.co.integrapratama.sdk.core.utils.CardUtil
import id.co.integrapratama.sdk.feature_print.core.PrintTemplateFactory
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementSummaryModel
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderGroupTemplateComponent
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderTemplateComponent
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderValueComponent

class SettlementPrintTemplateFactory(
    private val branchName: String,
    private val branchAddress: String,
    private val branchCity: String,
    private val terminalId: String,
    private val merchantId: String,
    private val date: String,
    private val time: String,
    private val batch: String,
    private val version: String,
    private val serialNumber: String,
    private val settlementSummaryModelList: List<SettlementSummaryModel>
) : PrintTemplateFactory() {
    override fun getPrintBasedOnTemplateParameterBuilder(): PrintBasedOnTemplateParameterBuilder {
        val line = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Line",
            value = ""
        )
        val headerImage = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "HeaderImage",
            value = "bni.png"
        )
        val branchName = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "BranchName",
            value = this.branchName
        )
        val branchAddress = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "BranchAddress",
            value = this.branchAddress
        )
        val branchCity = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "BranchCity",
            value = this.branchCity
        )
        val terminalId = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "TerminalId",
            value = this.terminalId
        )
        val merchantId = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "MerchantId",
            value = this.merchantId
        )
        val transactionType = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "TransactionType",
            value = "SETTLEMENT"
        )
        val date = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Date",
            value = this.date
        )
        val time = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Time",
            value = this.time
        )
        val batch = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Batch",
            value = this.batch
        )
        val labelTransactionTotalByIssuer = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelTransactionTotalByIssuer",
            value = "TRANSACTION TOTAL BY ISSUER"
        )
        val labelCreditCard = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelCreditCard",
            value = "KARTU DEBIT"
        )
        val labelSale = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelSale",
            value = "SALE"
        )
        val labelVoid = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelVoid",
            value = "VOID"
        )
        val labelTotal = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelTotal",
            value = "TOTAL"
        )
        val labelOtherBank = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelOtherBank",
            value = "BANK LAIN"
        )
        val labelTotalTransactionDebit = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelTotalTransactionDebit",
            value = "TOTAL TRANSAKSI DEBIT"
        )
        val labelTotalTransactionCredit = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelTotalTransactionCredit",
            value = "TOTAL TRANSAKSI KREDIT"
        )
        val labelSettlementClosed = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelTotalTransactionCredit",
            value = "SETTLEMENT CLOSED"
        )
        val settlementSummaryGroupTemplateComponent = mutableListOf<PrintBasedOnTemplateParameterBuilderGroupTemplateComponent>()

        val builder = PrintBasedOnTemplateParameterBuilder()
            .addAllValueComponent(
                listOf(
                    headerImage,
                    branchName,
                    branchAddress,
                    branchCity,
                    terminalId,
                    merchantId,
                    transactionType,
                    date,
                    time,
                    batch,
                    labelTransactionTotalByIssuer,
                    labelCreditCard,
                    labelSale,
                    labelVoid,
                    labelTotal,
                    labelOtherBank,
                    labelTotalTransactionDebit,
                    labelTotalTransactionCredit,
                    labelSettlementClosed
                )
            )
            .addAllTemplateComponent(
                listOf(

                )
            )

        return builder
    }
}