/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkCheckBox
import zakadabar.stack.frontend.builtin.input.ZkTextInput

open class ConstraintEditor : ZkElement() {
    open fun toDto(): BoConstraint? {
        TODO()
    }
}

class Optional : ZkElement() {
    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + row {
            + div { + "Optional:" } css kodomatStyles.editorLabel
            + ZkCheckBox()
        }
    }

    val value: Boolean
        get() = first<ZkCheckBox>().checked
}

class ConstraintBooleanEditor(
    private val constraintType: BoConstraintType,
    private val skipWhen: Boolean
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css kodomatStyles.editorLabel
            + ZkCheckBox(checked = skipWhen)
        }
    }

    override fun toDto(): BoConstraint? {
        val value = first<ZkCheckBox>().checked
        return if (value == skipWhen) null else BooleanBoConstraint(constraintType, value)
    }
}

class ConstraintDoubleEditor(
    private val constraintType: BoConstraintType
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css kodomatStyles.editorLabel
            + ZkTextInput() css kodomatStyles.mediumInput
        }
    }

    override fun toDto(): BoConstraint? {
        val value = first<ZkTextInput>().value.toDoubleOrNull()
        return if (value == null) null else DoubleBoConstraint(constraintType, value)
    }

}

class ConstraintIntEditor(
    private val constraintType: BoConstraintType
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css kodomatStyles.editorLabel
            + ZkTextInput() css kodomatStyles.mediumInput
        }
    }

    override fun toDto(): BoConstraint? {
        val value = first<ZkTextInput>().value.toIntOrNull()
        return if (value == null) null else IntBoConstraint(constraintType, value)
    }

}

class ConstraintLongEditor(
    private val constraintType: BoConstraintType
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css kodomatStyles.editorLabel
            + ZkTextInput() css kodomatStyles.mediumInput
        }
    }

    override fun toDto(): BoConstraint? {
        val value = first<ZkTextInput>().value.toLongOrNull()
        return if (value == null) null else LongBoConstraint(constraintType, value)
    }

}

class ConstraintStringEditor(
    private val constraintType: BoConstraintType
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css kodomatStyles.editorLabel
            + ZkTextInput() css kodomatStyles.mediumInput
        }
    }

    override fun toDto(): BoConstraint? {
        val value = first<ZkTextInput>().value
        return if (value.isEmpty()) null else StringBoConstraint(constraintType, value)
    }

}