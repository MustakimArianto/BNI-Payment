package id.co.integrapratama.sdk.feature_sale.core

import id.co.integrapratama.sdk.core.utils.CardUtil
import id.co.integrapratama.sdk.feature_print.core.PrintTemplateFactory
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderGroupTemplateComponent
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderTemplateComponent
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderValueComponent

class SalePrintTemplateFactory(
    private val branchName: String,
    private val branchAddress: String,
    private val branchCity: String,
    private val terminalId: String,
    private val merchantId: String,
    private val cardType: String,
    private val exp: String,
    private val cardNumber: String,
    private val cardMethod: String,
    private val date: String,
    private val time: String,
    private val batch: String,
    private val trace: String,
    private val ref: String,
    private val appr: String,
    private val amount: String,
    private val version: String,
) : PrintTemplateFactory() {
    override fun getPrintBasedOnTemplateParameterBuilder(): PrintBasedOnTemplateParameterBuilder {
        val headerImageValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "HeaderImage",
                value = "bni.png"
            )
        val branchNameValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "BranchName",
                value = branchName
            )
        val branchAddressValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "BranchAddress",
                value = branchAddress
            )
        val branchCityValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "BranchCity",
                value = branchCity
            )
        val terminalIdValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "TerminalId",
                value = terminalId
            )
        val merchantIdValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "MerchantId",
                value = merchantId
            )
        val cardTypeValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "CardType",
                value = cardType
            )
        val expValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Exp",
                value = exp
            )
        val cardNumberValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "CardNumberWithType",
                value = fun(): String {
                    val resultList = mutableListOf<String>()
                    if (cardNumber.isNotBlank()) {
                        val cardNumber = CardUtil.panMasking(cardNumber, cardNumber)
                        if (cardNumber.isNullOrBlank().not()) {
                            resultList.add(cardNumber)
                        }
                    }
                    if (cardMethod.isNotBlank()) {
                        resultList.add("(${cardMethod})")
                    }
                    return resultList.joinToString(" ")
                }()
            )
        val labelValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Label",
                value = "SALE"
            )
        val dateValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Date",
                value = date
            )
        val timeValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Time",
                value = time
            )
        val batchValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Batch",
                value = batch
            )
        val traceValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Trace",
                value = trace
            )
        val refValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Ref",
                value = ref
            )
        val apprValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Appr",
                value = appr
            )
        val amountValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Amount",
                value = amount
            )
        val pinVerificationSuccessLabelValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "PinVerificationSuccessLabel",
                value = "*** PIN VERIFICATION SUCCESS ***"
            )
        val totalAmountAgreementValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "TotalAmountAgreementLabel",
                value = "I AGREE TO PAY ABOVE TOTAL AMOUNT"
            )
        val accordingToCardLabelValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "AccordingToCardLabel",
                value = "ACCORDING TO CARD ISSUER AGREEMENT"
            )
        val copyLabelValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "CopyLabel",
                value = "*** CUSTOMER COPY ***"
            )
        val versionPrintValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Version",
                value = version
            )
        val linePrintValue =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Line",
                value = ""
            )

        val builder = PrintBasedOnTemplateParameterBuilder()
            .addAllValueComponent(
                listOf(
                    headerImageValue,
                    branchNameValue,
                    branchAddressValue,
                    branchCityValue,
                    terminalIdValue,
                    merchantIdValue,
                    cardTypeValue,
                    expValue,
                    cardNumberValue,
                    labelValue,
                    dateValue,
                    timeValue,
                    batchValue,
                    traceValue,
                    refValue,
                    apprValue,
                    amountValue,
                    pinVerificationSuccessLabelValue,
                    totalAmountAgreementValue,
                    accordingToCardLabelValue,
                    copyLabelValue,
                    versionPrintValue,
                    linePrintValue
                )
            )
            .addAllTemplateComponent(
                listOf(
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = headerImageValue,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchNameValue,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchAddressValue,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchCityValue,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = terminalIdValue,
                        key = "TERMINAL ID",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = merchantIdValue,
                        key = "MERCHANT ID",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = cardTypeValue,
                                key = "CARD TYPE",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = expValue,
                                key = "EXP",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = cardNumberValue,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = labelValue,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "L"
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = batchValue,
                                key = "BATCH",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = traceValue,
                                key = "TRACE",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = dateValue,
                                key = "DATE",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = timeValue,
                                key = "TIME",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = refValue,
                                key = "REF#",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = apprValue,
                                key = "APPR",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = amountValue,
                        key = "Amount",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = pinVerificationSuccessLabelValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = totalAmountAgreementValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = accordingToCardLabelValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = copyLabelValue,
                                key = "",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = versionPrintValue,
                                key = "",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintValue,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    )
                )
            )
        return builder
    }
}