package id.co.integrapratama.sdk.feature_installment.core

import id.co.integrapratama.sdk.core.utils.CardUtil
import id.co.integrapratama.sdk.feature_print.core.PrintTemplateFactory
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderGroupTemplateComponent
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderTemplateComponent
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilderValueComponent

class InstallmentPrintTemplateFactory(
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
    private val serialNumber: String
) : PrintTemplateFactory() {
    override fun getPrintBasedOnTemplateParameterBuilder(): PrintBasedOnTemplateParameterBuilder {
        val branchNamePrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "BranchName",
                value = branchName
            )
        val branchAddressPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "BranchAddress",
                value = branchAddress
            )
        val branchCityPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "BranchCity",
                value = branchCity
            )
        val terminalIdPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "TerminalId",
                value = terminalId
            )
        val merchantIdPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "MerchantId",
                value = merchantId
            )
        val cardTypePrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "CardType",
                value = cardType
            )
        val expPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Exp",
                value = exp
            )
        val cardNumberWithTypePrintBasedOnTemplateParameterBuilderValueComponent =
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
        val labelPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Label",
                value = "INSTALLMENT"
            )
        val datePrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Date",
                value = date
            )
        val timePrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Time",
                value = time
            )
        val batchPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Batch",
                value = batch
            )
        val tracePrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Trace",
                value = trace
            )
        val refPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Ref",
                value = ref
            )
        val apprPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Appr",
                value = appr
            )
        val amountPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Amount",
                value = amount
            )
        val pinVerificationSuccessLabelPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "PinVerificationSuccessLabel",
                value = "*** PIN VERIFICATION SUCCESS ***"
            )
        val totalAmountAgreementLabelPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "TotalAmountAgreementLabel",
                value = "I AGREE TO PAY ABOVE TOTAL AMOUNT"
            )
        val accordingToCardLabelPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "AccordingToCardLabel",
                value = "ACCORDING TO CARD ISSUER AGREEMENT"
            )
        val copyLabelPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "CopyLabel",
                value = "*** CUSTOMER COPY ***"
            )
        val versionPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Version",
                value = version
            )
        val machineSerialNumberPrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "MachineSerialNumber",
                value = serialNumber
            )
        val linePrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "Line",
                value = ""
            )
        val headerImagePrintBasedOnTemplateParameterBuilderValueComponent =
            PrintBasedOnTemplateParameterBuilderValueComponent(
                key = "HeaderImage",
                value = "bni.png"
            )
        val builder = PrintBasedOnTemplateParameterBuilder()
            .addAllValueComponent(
                listOf(
                    headerImagePrintBasedOnTemplateParameterBuilderValueComponent,
                    branchNamePrintBasedOnTemplateParameterBuilderValueComponent,
                    branchAddressPrintBasedOnTemplateParameterBuilderValueComponent,
                    branchCityPrintBasedOnTemplateParameterBuilderValueComponent,
                    terminalIdPrintBasedOnTemplateParameterBuilderValueComponent,
                    merchantIdPrintBasedOnTemplateParameterBuilderValueComponent,
                    cardTypePrintBasedOnTemplateParameterBuilderValueComponent,
                    expPrintBasedOnTemplateParameterBuilderValueComponent,
                    cardNumberWithTypePrintBasedOnTemplateParameterBuilderValueComponent,
                    labelPrintBasedOnTemplateParameterBuilderValueComponent,
                    datePrintBasedOnTemplateParameterBuilderValueComponent,
                    timePrintBasedOnTemplateParameterBuilderValueComponent,
                    batchPrintBasedOnTemplateParameterBuilderValueComponent,
                    tracePrintBasedOnTemplateParameterBuilderValueComponent,
                    refPrintBasedOnTemplateParameterBuilderValueComponent,
                    apprPrintBasedOnTemplateParameterBuilderValueComponent,
                    amountPrintBasedOnTemplateParameterBuilderValueComponent,
                    pinVerificationSuccessLabelPrintBasedOnTemplateParameterBuilderValueComponent,
                    totalAmountAgreementLabelPrintBasedOnTemplateParameterBuilderValueComponent,
                    accordingToCardLabelPrintBasedOnTemplateParameterBuilderValueComponent,
                    copyLabelPrintBasedOnTemplateParameterBuilderValueComponent,
                    versionPrintBasedOnTemplateParameterBuilderValueComponent,
                    machineSerialNumberPrintBasedOnTemplateParameterBuilderValueComponent,
                    linePrintBasedOnTemplateParameterBuilderValueComponent,
                    headerImagePrintBasedOnTemplateParameterBuilderValueComponent,
                )
            )
            .addAllTemplateComponent(
                listOf(
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = headerImagePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchNamePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchAddressPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchCityPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = terminalIdPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "TERMINAL ID",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = merchantIdPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "MERCHANT ID",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = cardTypePrintBasedOnTemplateParameterBuilderValueComponent,
                                key = "CARD TYPE",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = expPrintBasedOnTemplateParameterBuilderValueComponent,
                                key = "EXP",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = cardNumberWithTypePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = labelPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "L"
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = datePrintBasedOnTemplateParameterBuilderValueComponent,
                                key = "DATE",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = timePrintBasedOnTemplateParameterBuilderValueComponent,
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
                                valueComponent = batchPrintBasedOnTemplateParameterBuilderValueComponent,
                                key = "BATCH",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = tracePrintBasedOnTemplateParameterBuilderValueComponent,
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
                                valueComponent = refPrintBasedOnTemplateParameterBuilderValueComponent,
                                key = "REF#",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = apprPrintBasedOnTemplateParameterBuilderValueComponent,
                                key = "APPR",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = amountPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "Amount",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = pinVerificationSuccessLabelPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = totalAmountAgreementLabelPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = accordingToCardLabelPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = copyLabelPrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = machineSerialNumberPrintBasedOnTemplateParameterBuilderValueComponent,
                                key = "",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = versionPrintBasedOnTemplateParameterBuilderValueComponent,
                                key = "",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = linePrintBasedOnTemplateParameterBuilderValueComponent,
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