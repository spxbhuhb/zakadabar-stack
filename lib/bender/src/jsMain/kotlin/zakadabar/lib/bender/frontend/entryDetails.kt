/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend

import zakadabar.lib.bender.*
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkTextInput

open class EntryDetails : ZkElement() {

    val optional
        get() = first<Optional>().value

    val constraints
        get() = find<ConstraintEditor>().mapNotNull { it.toDto() }

    open fun generator(name: String, boDescriptor: BoDescriptor): PropertyGenerator {
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

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        BooleanPropertyGenerator(
            boDescriptor,
            BooleanBoProperty(name, optional, constraints, defaultValue = false, value = false)
        )

}

class DoubleDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintDoubleEditor(BoConstraintType.Min)
            + ConstraintDoubleEditor(BoConstraintType.Max)
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        DoublePropertyGenerator(
            boDescriptor,
            DoubleBoProperty(name, optional, constraints, 0.0, 0.0)
        )

}

class EnumDetails : EntryDetails() {

    private val enumName = ZkTextInput()

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()

            + row {
                + div { + "Enum Name" } css benderStyles.editorLabel
                + enumName css benderStyles.largeInput
            }
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        EnumPropertyGenerator(
            boDescriptor,
            EnumBoProperty(name, optional, constraints, enumName.value, emptyList(), null, null)
        )

}

class IntDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintIntEditor(BoConstraintType.Min)
            + ConstraintIntEditor(BoConstraintType.Max)
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        IntPropertyGenerator(
            boDescriptor,
            IntBoProperty(name, optional, constraints, 0, 0)
        )

}

class InstantDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        InstantPropertyGenerator(
            boDescriptor,
            InstantBoProperty(name, optional, constraints, null, null)
        )

}

class LongDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintLongEditor(BoConstraintType.Min)
            + ConstraintLongEditor(BoConstraintType.Max)
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        LongPropertyGenerator(
            boDescriptor,
            LongBoProperty(name, optional, constraints, 0, 0)
        )

}

class RecordIdDetails : EntryDetails() {

    private val recordType = ZkTextInput()

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()

            + row {
                + div { + "Record Type:" } css benderStyles.editorLabel
                + recordType css benderStyles.largeInput
            }
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        EntityIdPropertyGenerator(
            boDescriptor,
            EntityIdBoProperty(name, optional, constraints, recordType.value, EntityId(), EntityId())
        )

}

class SecretDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintIntEditor(BoConstraintType.Min)
            + ConstraintIntEditor(BoConstraintType.Max)
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        SecretPropertyGenerator(
            boDescriptor,
            SecretBoProperty(name, optional, constraints, Secret(""), Secret(""))
        )

}

class StringDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
            + ConstraintBooleanEditor(BoConstraintType.Blank, true)
            + ConstraintIntEditor(BoConstraintType.Min)
            + ConstraintIntEditor(BoConstraintType.Max)
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        StringPropertyGenerator(
            boDescriptor,
            StringBoProperty(name, optional, constraints, "", "")
        )

}

class UuidDetails : EntryDetails() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        UuidPropertyGenerator(
            boDescriptor,
            UuidBoProperty(name, optional, constraints, null, null)
        )

}