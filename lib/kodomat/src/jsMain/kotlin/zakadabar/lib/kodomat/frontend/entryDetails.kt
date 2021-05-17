/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkTextInput

open class EntryDetails : ZkElement() {

    val optional
        get() = findFirst<Optional>().value

    val constraints
        get() = find<ConstraintEditor>().mapNotNull { it.toDto() }

    open fun generator(name: String, descriptor: DescriptorDto): PropertyGenerator {
        TODO()
    }
}

class BooleanDetails : EntryDetails() {

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

class DoubleDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintDoubleEditor(ConstraintType.Min)
            + ConstraintDoubleEditor(ConstraintType.Max)
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        DoublePropertyGenerator(
            descriptor,
            DoublePropertyDto(name, optional, constraints, 0.0, 0.0)
        )

}

class EnumDetails : EntryDetails() {

    private val enumName = ZkTextInput()

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()

            + row {
                + div { + "Enum Name" } css kodomatStyles.editorLabel
                + enumName css kodomatStyles.largeInput
            }
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        EnumPropertyGenerator(
            descriptor,
            EnumPropertyDto(name, optional, constraints, enumName.value, emptyList(), null, null)
        )

}

class IntDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintIntEditor(ConstraintType.Min)
            + ConstraintIntEditor(ConstraintType.Max)
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        IntPropertyGenerator(
            descriptor,
            IntPropertyDto(name, optional, constraints, 0, 0)
        )

}

class InstantDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        InstantPropertyGenerator(
            descriptor,
            InstantPropertyDto(name, optional, constraints, null, null)
        )

}

class LongDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintLongEditor(ConstraintType.Min)
            + ConstraintLongEditor(ConstraintType.Max)
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        LongPropertyGenerator(
            descriptor,
            LongPropertyDto(name, optional, constraints, 0, 0)
        )

}

class RecordIdDetails : EntryDetails() {

    private val recordType = ZkTextInput()

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()

            + row {
                + div { + "Record Type:" } css kodomatStyles.editorLabel
                + recordType css kodomatStyles.largeInput
            }
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        RecordIdPropertyGenerator(
            descriptor,
            RecordIdPropertyDto(name, optional, constraints, recordType.value, EmptyRecordId(), EmptyRecordId())
        )

}

class SecretDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintIntEditor(ConstraintType.Min)
            + ConstraintIntEditor(ConstraintType.Max)
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        SecretPropertyGenerator(
            descriptor,
            SecretPropertyDto(name, optional, constraints, Secret(""), Secret(""))
        )

}

class StringDetails : EntryDetails() {

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

class UuidDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
        }
    }

    override fun generator(name: String, descriptor: DescriptorDto) =
        UuidPropertyGenerator(
            descriptor,
            UuidPropertyDto(name, optional, constraints, null, null)
        )

}