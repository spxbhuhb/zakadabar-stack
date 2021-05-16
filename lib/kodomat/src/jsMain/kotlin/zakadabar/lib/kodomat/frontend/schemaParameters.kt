/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkCheckBox
import zakadabar.stack.frontend.builtin.input.ZkTextInput

open class SchemaParameter : ZkElement() {
    open fun generator(name: String, descriptor: DescriptorDto) : PropertyGenerator {
        TODO()
    }
}

class RecordIdSchemaParameters : SchemaParameter() {

    val recordType = ZkTextInput()
    val optional = ZkCheckBox()

    override fun onCreate() {
        super.onCreate()

        + grid {
            gridTemplateColumns = "repeat(4 max-content)"
            gridGap = 10
            + "optional"
            + optional
            + "recordType "
            + recordType
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto): PropertyGenerator {
        return if (optional.checked) {
            OptRecordIdGenerator(
                descriptor,
                OptRecordIdPropertyDto(name, emptyList(), recordType.value, EmptyRecordId(), EmptyRecordId())
            )
        } else {
            RecordIdGenerator(
                descriptor,
                RecordIdPropertyDto(name, emptyList(), recordType.value, EmptyRecordId(), EmptyRecordId())
            )
        }
    }
}

class BooleanSchemaParameters : SchemaParameter() {

    val optional = ZkCheckBox()

    override fun onCreate() {
        super.onCreate()

        + grid {
            gridTemplateColumns = "repeat(4, max-content)"
            gridGap = 10
            + "optional"
            + optional
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto): PropertyGenerator {
        return if (optional.checked) {
            OptBooleanGenerator(
                descriptor,
                OptBooleanPropertyDto(name, emptyList(), false, false)
            )
        } else {
            BooleanGenerator(
                descriptor,
                BooleanPropertyDto(name, emptyList(), false, false)
            )
        }
    }
}

class StringSchemaParameters : SchemaParameter() {

    val optional = ZkCheckBox()
    val blank = ZkCheckBox()
    val min = ZkTextInput()
    val max = ZkTextInput()

    override fun onCreate() {
        super.onCreate()

        + grid {
            gridTemplateColumns = "repeat(8, max-content)"
            gridGap = 10
            + "optional"
            + optional
            + "blank"
            + blank
            + "min"
            + min css kodomatStyles.smallInput
            + "max"
            + max css kodomatStyles.mediumInput
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto): PropertyGenerator {
        return if (optional.checked) {
            OptBooleanGenerator(
                descriptor,
                OptBooleanPropertyDto(name, emptyList(), false, false)
            )
        } else {
            BooleanGenerator(
                descriptor,
                BooleanPropertyDto(name, emptyList(), false, false)
            )
        }
    }
}