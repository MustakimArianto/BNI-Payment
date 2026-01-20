package id.co.integrapratama.sdk.feature_settlement.core

import id.co.integrapratama.sdk.feature_print.core.PrintTemplateFactory
import id.co.integrapratama.sdk.feature_settlement.domain.SettlementSummaryGroupModel
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.BasePrintBasedOnTemplateParameterBuilderTemplateComponent
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
    private val settlementSummaryGroupModelList: List<SettlementSummaryGroupModel>
) : PrintTemplateFactory() {
    override fun getPrintBasedOnTemplateParameterBuilder(): PrintBasedOnTemplateParameterBuilder {
        val line = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Line",
            value = ""
        )
        val separator = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Separator",
            value = "-------------------------------------"
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
        val labelSettlement = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelSettlement",
            value = "SETTLEMENT"
        )
        val labelTransactionTotalByIssuer = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelTransactionTotalByIssuer",
            value = "TRANSACTION TOTAL BY ISSUER"
        )
        val labelSettlementClosed = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "LabelTotalTransactionCredit",
            value = "SETTLEMENT CLOSED"
        )
        val settlementSummaryGroupValueComponent = mutableListOf<PrintBasedOnTemplateParameterBuilderValueComponent>()
        val settlementSummaryGroupTemplateComponent = mutableListOf<BasePrintBasedOnTemplateParameterBuilderTemplateComponent>()

        for (settlementSummaryGroupModelIndexedValue in settlementSummaryGroupModelList.withIndex()) {
            val i = settlementSummaryGroupModelIndexedValue.index
            val settlementSummaryGroupModel = settlementSummaryGroupModelIndexedValue.value
            settlementSummaryGroupTemplateComponent.add(
                PrintBasedOnTemplateParameterBuilderTemplateComponent(
                    valueComponent = line,
                    key = "",
                    value = "",
                    alignment = "L",
                    font = "S"
                ),
            )
            val summaryGroupTitle = PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "SummaryGroupTitle-$i",
                value = settlementSummaryGroupModel.title
            )
            settlementSummaryGroupTemplateComponent.add(
                PrintBasedOnTemplateParameterBuilderTemplateComponent(
                    valueComponent = summaryGroupTitle,
                    key = "",
                    value = "",
                    alignment = "L",
                    font = "M"
                ),
            )

            val summaryModelList = settlementSummaryGroupModel.summaryModelList
            for (summaryModelIndexedValue in summaryModelList.withIndex()) {
                val i2 = summaryModelIndexedValue.index
                val summaryModel = summaryModelIndexedValue.value

                val summaryTitle = PrintBasedOnTemplateParameterBuilderValueComponent(
                    key = "SummaryTitle-$i-$i2",
                    value = summaryModel.title
                )
                settlementSummaryGroupTemplateComponent.add(
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = summaryTitle,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "M"
                    ),
                )

                val subSummaryModelList = summaryModel.subSummaryList
                for (subSummaryModelIndexedValue in subSummaryModelList.withIndex()) {
                    val i3 = subSummaryModelIndexedValue.index
                    val subSummaryModel = subSummaryModelIndexedValue.value
                    val newSubSummaryValueComponentList = mutableListOf<PrintBasedOnTemplateParameterBuilderValueComponent>()
                    val newSubSummaryTemplateComponentList = mutableListOf<PrintBasedOnTemplateParameterBuilderTemplateComponent>()

                    var addingStep = 1
                    fun addNewTemplateComponentFromValueList(value: String, rule: (() -> Boolean)? = null) {
                        if (addingStep > 3) {
                            return
                        }
                        var allowToBeAdded = true
                        if (rule != null) {
                            allowToBeAdded = rule()
                        }
                        if (allowToBeAdded) {
                            val newSubSummaryValue = PrintBasedOnTemplateParameterBuilderValueComponent(
                                key = "SubSummaryValue-$i-$i2-$i3-$addingStep",
                                value = value
                            )
                            newSubSummaryValueComponentList.add(newSubSummaryValue)
                            newSubSummaryTemplateComponentList.add(
                                PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                    valueComponent = newSubSummaryValue,
                                    key = "",
                                    value = "",
                                    alignment = fun (): String {
                                        return when (addingStep) {
                                            1 -> "L"
                                            2 -> "C"
                                            3 -> "R"
                                            else -> ""
                                        }
                                    }(),
                                    font = "S"
                                ),
                            )
                        }
                        addingStep++
                    }

                    addNewTemplateComponentFromValueList(subSummaryModel.title) {
                        subSummaryModel.title.isNotBlank()
                    }
                    addNewTemplateComponentFromValueList(subSummaryModel.count.toString()) {
                        subSummaryModel.count != null
                    }
                    addNewTemplateComponentFromValueList(subSummaryModel.amount.toString())

                    settlementSummaryGroupValueComponent.addAll(
                        newSubSummaryValueComponentList
                    )
                    settlementSummaryGroupTemplateComponent.add(
                        PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                            templateComponentList = newSubSummaryTemplateComponentList
                        )
                    )
                }
            }
        }

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
                    labelSettlementClosed
                )
            )
            .addAllValueComponent(
                settlementSummaryGroupValueComponent
            )
            .addAllTemplateComponent(
                listOf(
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = headerImage,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = line,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchName,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchAddress,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchCity,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = line,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = labelSettlement,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "M"
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = mutableListOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = terminalId,
                                key = "",
                                value = "TID",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = merchantId,
                                key = "",
                                value = "MID",
                                alignment = "R",
                                font = "S"
                            )
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = mutableListOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = date,
                                key = "",
                                value = "DATE",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = time,
                                key = "",
                                value = "TIME",
                                alignment = "R",
                                font = "S"
                            )
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = batch,
                        key = "",
                        value = "BATCH",
                        alignment = "L",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = line,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = labelTransactionTotalByIssuer,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "M"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = separator,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "S"
                    ),
                )
            )
            .addAllTemplateComponent(
                settlementSummaryGroupTemplateComponent
            )
            .addAllTemplateComponent(
                listOf(
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = line,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = labelSettlementClosed,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = line,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "S"
                    ),
                )
            )

        return builder
    }
}