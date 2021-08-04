/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend.property

import zakadabar.lib.bender.*
import zakadabar.lib.bender.frontend.benderStyles
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkTextInput

open class PropertyConstraintsEditor : ZkElement() {

    val optional
        get() = first<Optional>().value

    val constraints
        get() = find<SingleConstraintEditor>().mapNotNull { it.toBoConstraint() }

    open fun update(property: BoProperty) {
        firstOrNull<Optional>()?.value = property.optional
        find<SingleConstraintEditor>().forEach {
            println(it)
            it.update(property.constraints) }
    }

    open fun generator(name: String, boDescriptor: BoDescriptor): PropertyGenerator {
        TODO()
    }
}

class BooleanPropertyConstraintsEditor : PropertyConstraintsEditor() {

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

class DoublePropertyConstraintsEditor : PropertyConstraintsEditor() {

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

class EnumPropertyConstraintsEditor : PropertyConstraintsEditor() {

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

    override fun update(property: BoProperty) {
        super.update(property)
        if (property is EnumBoProperty) enumName.value = property.enumName
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        EnumPropertyGenerator(
            boDescriptor,
            EnumBoProperty(name, optional, constraints, enumName.value, emptyList(), null, null)
        )

}

class IntPropertyConstraintsEditor : PropertyConstraintsEditor() {

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

class InstantPropertyConstraintsEditor : PropertyConstraintsEditor() {

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


class LocalDatePropertyConstraintsEditor : PropertyConstraintsEditor() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        LocalDatePropertyGenerator(
            boDescriptor,
            LocalDateBoProperty(name, optional, constraints, null, null)
        )

}

class LocalDateTimePropertyConstraintsEditor : PropertyConstraintsEditor() {

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()
        }
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        LocalDateTimePropertyGenerator(
            boDescriptor,
            LocalDateTimeBoProperty(name, optional, constraints, null, null)
        )

}

class LongPropertyConstraintsEditor : PropertyConstraintsEditor() {

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

class EntityIdPropertyConstraintsEditor : PropertyConstraintsEditor() {

    private val entityType = ZkTextInput()

    override fun onCreate() {
        super.onCreate()

        + row {
            + Optional()

            + row {
                + div { + "Entity Type:" } css benderStyles.editorLabel
                + entityType css benderStyles.largeInput
            }
        }
    }

    override fun update(property: BoProperty) {
        super.update(property)
        if (property is EntityIdBoProperty) entityType.value = property.kClassName
    }

    override fun generator(name: String, boDescriptor: BoDescriptor) =
        EntityIdPropertyGenerator(
            boDescriptor,
            EntityIdBoProperty(name, optional, constraints, entityType.value, EntityId(), EntityId())
        )

}

class SecretPropertyConstraintsEditor : PropertyConstraintsEditor() {

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

class StringPropertyConstraintsEditor: PropertyConstraintsEditor() {

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

class UuidPropertyConstraintsEditor : PropertyConstraintsEditor() {

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