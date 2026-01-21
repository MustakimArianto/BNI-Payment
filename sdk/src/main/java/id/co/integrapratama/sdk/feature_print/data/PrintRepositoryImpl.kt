package id.co.integrapratama.sdk.feature_print.data

import android.content.Context
import id.co.integrapratama.sdk.feature_print.core.PrintTemplateFactory
import id.co.integrapratama.sdk.feature_print.domain.PrintRepository
import id.co.payment2go.terminalsdkhelper.common.printer.ImageDataParameter
import id.co.payment2go.terminalsdkhelper.common.printer.PrinterUtility
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameter.PrintBasedOnTemplateBuilderParameter
import id.co.payment2go.terminalsdkhelper.common.printer.printbasedontemplateparameterbuilder.PrintBasedOnTemplateParameterBuilder
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import id.co.payment2go.terminalsdkhelper.core.util.Util
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PrintRepositoryImpl(
    private val printerUtility: PrinterUtility,
    context: Context,
) : PrintRepository {
    private val ctx = context

    companion object {
        private const val TAG = "PrintRepositoryImpl"
    }

    override suspend fun printWithBuilder(
        builder: PrintBasedOnTemplateParameterBuilder,
        imageData: ((ImageDataParameter) -> ByteArray?)?
    ): Flow<Resource<Unit>> {
        return callbackFlow {
            printerUtility.printBasedOnTemplate(
                PrintBasedOnTemplateBuilderParameter(
                    builder = builder,
                    imageData = imageData
                ) {
                    trySend(it)

                    if (it is Resource.Success || it is Resource.Error) {
                        close()
                    }
                }
            )

            awaitClose {}
        }
    }

    override suspend fun printWithBuilder(builder: PrintBasedOnTemplateParameterBuilder): Flow<Resource<Unit>> {
        return printWithBuilder(
            builder = builder,
            imageData = {
                if (it.headerImageValue == "bni.png") {
                    Util.readAssetsFile(ctx, it.headerImageValue)
                }
                null
            }
        )
    }

    override suspend fun printWithTemplateFactory(templateFactory: PrintTemplateFactory): Flow<Resource<Unit>> {
        return printWithBuilder(
            builder = templateFactory.getPrintBasedOnTemplateParameterBuilder()
        )
    }
}