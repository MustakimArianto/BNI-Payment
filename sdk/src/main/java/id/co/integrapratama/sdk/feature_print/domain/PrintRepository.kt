package id.co.integrapratama.sdk.feature_print.domain

import id.co.integrapratama.sdk.feature_print.core.PrintTemplateFactory
import id.co.payment2go.terminalsdkhelper.common.printer.ImageDataParameter
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface PrintRepository {
    suspend fun printWithBuilder(
        builder: PrintBasedOnTemplateParameterBuilder,
        imageData: ((ImageDataParameter) -> ByteArray?)? = null
    ): Flow<Resource<Unit>>

    suspend fun printWithBuilder(
        builder: PrintBasedOnTemplateParameterBuilder
    ): Flow<Resource<Unit>>

    suspend fun printWithTemplateFactory(
        templateFactory: PrintTemplateFactory,
    ): Flow<Resource<Unit>>
}