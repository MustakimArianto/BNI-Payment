package id.co.integrapratama.sdk.feature_print.core

import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.core.util.Resource

abstract class PrintTemplateFactory {
    abstract fun getPrintBasedOnTemplateParameterBuilder(): PrintBasedOnTemplateParameterBuilder

    fun getPrintBasedOnTemplateParameter(
        startPrintCallback: ((Resource<Unit>) -> Unit)? = null
    ) = getPrintBasedOnTemplateParameterBuilder().toPrintBasedOnTemplateParameter {
        startPrintCallback?.invoke(it)
    }
}