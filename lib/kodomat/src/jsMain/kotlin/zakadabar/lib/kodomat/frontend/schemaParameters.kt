/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkTextInput

open class SchemaParameter : ZkElement() {

    val optional
        get() = findFirst<Optional>().value

    val constraints
        get() = find<ConstraintEditor>().mapNotNull { it.toDto() }

    open fun generator(name: String, descriptor: DescriptorDto): PropertyGenerator {
        TODO()
    }
}


class RecordIdSchemaParameters : SchemaParameter() {

    val recordType = ZkTextInput()

    override fun onCreate() {
        super.onCreate()

        + row {
            + div { + "Record Type" } css kodomatStyles.editorLabel
            + recordType
            + Optional()
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        RecordIdPropertyGenerator(
            descriptor,
            RecordIdPropertyDto(name, optional, constraints, recordType.value, EmptyRecordId(), EmptyRecordId())
        )

}

class BooleanSchemaParameters : SchemaParameter() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        BooleanPropertyGenerator(
            descriptor,
            BooleanPropertyDto(name, optional, constraints, defaultValue = false, value = false)
        )

}

class StringSchemaParameters : SchemaParameter() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintBooleanEditor(ConstraintType.Blank, true)
            + ConstraintIntEditor(ConstraintType.Min)
            + ConstraintIntEditor(ConstraintType.Max)
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        StringPropertyGenerator(
            descriptor,
            StringPropertyDto(name, optional, constraints, "", "")
        )

}