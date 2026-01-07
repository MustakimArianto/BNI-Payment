package id.co.integrapratama.sdk.core.iso8583

import id.co.integrapratama.iso8583sdk.IsoSpecConfiguration
import id.co.integrapratama.iso8583sdk.model.DataType
import id.co.integrapratama.iso8583sdk.model.FieldSpec
import id.co.integrapratama.iso8583sdk.model.LengthType

object IsoConfig {
    private val builder = IsoSpecConfiguration.Companion.builder()

    val genericSpec = builder
        .setFieldSpec(2, FieldSpec(LengthType.LLVAR, 99, DataType.NUMERIC))
        .setFieldSpec(3, FieldSpec(LengthType.FIXED, 6, DataType.NUMERIC))
        .setFieldSpec(4, FieldSpec(LengthType.FIXED, 12, DataType.NUMERIC))
        .setFieldSpec(7, FieldSpec(LengthType.FIXED, 10, DataType.NUMERIC))
        .setFieldSpec(11, FieldSpec(LengthType.FIXED, 6, DataType.NUMERIC))
        .setFieldSpec(12, FieldSpec(LengthType.FIXED, 6, DataType.NUMERIC))
        .setFieldSpec(13, FieldSpec(LengthType.FIXED, 4, DataType.NUMERIC))
        .setFieldSpec(14, FieldSpec(LengthType.FIXED, 4, DataType.NUMERIC))
        .setFieldSpec(18, FieldSpec(LengthType.FIXED, 5, DataType.NUMERIC))
        .setFieldSpec(22, FieldSpec(LengthType.FIXED, 3, DataType.NUMERIC))
        .setFieldSpec(23, FieldSpec(LengthType.FIXED, 3, DataType.NUMERIC))
        .setFieldSpec(24, FieldSpec(LengthType.FIXED, 3, DataType.NUMERIC))
        .setFieldSpec(25, FieldSpec(LengthType.FIXED, 2, DataType.NUMERIC))
        .setFieldSpec(32, FieldSpec(LengthType.LLVAR, 99, DataType.NUMERIC))
        .setFieldSpec(35, FieldSpec(LengthType.LLVAR, 99, DataType.ANS))
        .setFieldSpec(37, FieldSpec(LengthType.FIXED, 12, DataType.ALPHANUMERIC))
        .setFieldSpec(38, FieldSpec(LengthType.FIXED, 6, DataType.ALPHANUMERIC))
        .setFieldSpec(39, FieldSpec(LengthType.FIXED, 2, DataType.ALPHANUMERIC))
        .setFieldSpec(41, FieldSpec(LengthType.FIXED, 8, DataType.ANS))
        .setFieldSpec(42, FieldSpec(LengthType.FIXED, 15, DataType.ANS))
        .setFieldSpec(44, FieldSpec(LengthType.LLVAR, 99, DataType.ANS))
        .setFieldSpec(48, FieldSpec(LengthType.LLLVAR, 999, DataType.ANS)) // Need to add 4-LVar
        .setFieldSpec(52, FieldSpec(LengthType.FIXED, 16, DataType.BINARY))
        .setFieldSpec(55, FieldSpec(LengthType.LLLVAR, 999, DataType.ANS)) // 4-LVar
        .setFieldSpec(57, FieldSpec(LengthType.LLLVAR, 999, DataType.ANS)) // 4-LVar
        .setFieldSpec(61, FieldSpec(LengthType.LLLVAR, 999, DataType.ANS)) // 4-LVar
        .setFieldSpec(62, FieldSpec(LengthType.LLLVAR, 999, DataType.ANS)) // 4-LVar
        .setFieldSpec(63, FieldSpec(LengthType.LLLVAR, 999, DataType.ANS))
        .setFieldSpec(64, FieldSpec(LengthType.FIXED, 16, DataType.BINARY))
}