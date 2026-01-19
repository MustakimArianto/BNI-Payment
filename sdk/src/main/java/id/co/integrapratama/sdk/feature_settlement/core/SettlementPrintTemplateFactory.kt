package id.co.integrapratama.sdk.feature_settlement.core

import id.co.integrapratama.sdk.core.utils.CardUtil
import id.co.integrapratama.sdk.feature_print.core.PrintTemplateFactory
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
        val cardType = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "CardType",
            value = this.cardType
        )
        val exp = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Exp",
            value = this.exp
        )
        val cardNumberWithType = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "CardNumberWithType",
            value = fun(): String {
                val resultList = mutableListOf<String>()
                if (cardNumber.isNotBlank()) {
                    val cardNumber = CardUtil.panMasking(cardNumber, cardNumber)
                    if (cardNumber.isBlank().not()) {
                        resultList.add(cardNumber)
                    }
                }
                if (cardMethod.isNotBlank()) {
                    resultList.add("(${cardMethod})")
                }
                return resultList.joinToString(" ")
            }()
        )
        val label = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Label",
            value = "INSTALLMENT"
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
        val trace = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Trace",
            value = this.trace
        )
        val ref = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Ref",
            value = this.ref
        )
        val appr = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Appr",
            value = this.appr
        )
        val amount = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Amount",
            value = this.amount
        )
        val pinVerificationSuccessLabel = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "PinVerificationSuccessLabel",
            value = "*** PIN VERIFICATION SUCCESS ***"
        )
        val totalAmountAgreementLabel = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "TotalAmountAgreementLabel",
            value = "I AGREE TO PAY ABOVE TOTAL AMOUNT"
        )
        val accordingToCardLabel = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "AccordingToCardLabel",
            value = "ACCORDING TO CARD ISSUER AGREEMENT"
        )
        val copyLabel = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "CopyLabel",
            value = "*** CUSTOMER COPY ***"
        )
        val version = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Version",
            value = this.version
        )
        val machineSerialNumber = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "MachineSerialNumber",
            value = serialNumber
        )
        val line = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "Line",
            value = ""
        )
        val headerImage = PrintBasedOnTemplateParameterBuilderValueComponent(
            key = "HeaderImage",
            value = "bni.png"
        )
        val builder = PrintBasedOnTemplateParameterBuilder()
            .addAllValueComponent(
                listOf(
                    headerImage,
                    branchName,
                    branchAddress,
                    branchCity,
                    terminalId,
                    merchantId,
                    cardType,
                    exp,
                    cardNumberWithType,
                    label,
                    date,
                    time,
                    batch,
                    trace,
                    ref,
                    appr,
                    amount,
                    pinVerificationSuccessLabel,
                    totalAmountAgreementLabel,
                    accordingToCardLabel,
                    copyLabel,
                    version,
                    machineSerialNumber,
                    line,
                    headerImage,
                )
            )
            .addAllTemplateComponent(
                listOf(
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = headerImage,
                        key = "",
                        value = "Test",
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
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchAddress,
                        key = "",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = branchCity,
                        key = "",
                        value = "Test",
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
                        valueComponent = terminalId,
                        key = "TERMINAL ID",
                        value = "Test",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = merchantId,
                        key = "MERCHANT ID",
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
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = cardType,
                                key = "CARD TYPE",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = exp,
                                key = "EXP",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = cardNumberWithType,
                        key = "",
                        value = "",
                        alignment = "L",
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
                        valueComponent = label,
                        key = "",
                        value = "",
                        alignment = "L",
                        font = "L"
                    ),
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = date,
                                key = "DATE",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = time,
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
                                valueComponent = batch,
                                key = "BATCH",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = trace,
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
                                valueComponent = ref,
                                key = "REF#",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = appr,
                                key = "APPR",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = line,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = amount,
                        key = "Amount",
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
                        valueComponent = line,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = pinVerificationSuccessLabel,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = totalAmountAgreementLabel,
                        key = "",
                        value = "",
                        alignment = "C",
                        font = "S"
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = accordingToCardLabel,
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
                        valueComponent = copyLabel,
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
                    PrintBasedOnTemplateParameterBuilderGroupTemplateComponent(
                        templateComponentList = listOf(
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = machineSerialNumber,
                                key = "",
                                value = "",
                                alignment = "R",
                                font = "S"
                            ),
                            PrintBasedOnTemplateParameterBuilderTemplateComponent(
                                valueComponent = version,
                                key = "",
                                value = "",
                                alignment = "L",
                                font = "S"
                            ),
                        )
                    ),
                    PrintBasedOnTemplateParameterBuilderTemplateComponent(
                        valueComponent = line,
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