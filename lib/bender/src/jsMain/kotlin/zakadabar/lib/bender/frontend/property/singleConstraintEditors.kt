/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend.property

import zakadabar.lib.bender.frontend.benderStyles
import zakadabar.core.data.schema.descriptor.*
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.input.ZkCheckBox
import zakadabar.core.frontend.builtin.input.ZkTextInput

abstract class SingleConstraintEditor : ZkElement() {
    abstract fun toBoConstraint() : BoConstraint?
    abstract fun update(constraints : List<BoConstraint>)
}

class Optional : ZkElement() {
    override fun onCreate() {
        super.onCreate()

        + benderStyles.constraintEditor

        + row {
            + div { + "Optional:" } css benderStyles.editorLabel
            + ZkCheckBox()
        }
    }

    var value: Boolean
        get() = first<ZkCheckBox>().checked
        set(value) {
            first<ZkCheckBox>().checked = value
        }
}

class ConstraintBooleanEditor(
    private val constraintType: BoConstraintType,
    private val skipWhen: Boolean
) : SingleConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + benderStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css benderStyles.editorLabel
            + ZkCheckBox(checked = skipWhen)
        }
    }

    override fun toBoConstraint(): BoConstraint? {
        val value = first<ZkCheckBox>().checked
        return if (value == skipWhen) null else BooleanBoConstraint(constraintType, value)
    }

    override fun update(constraints: List<BoConstraint>) {
        val c = constraints.firstOrNull { it.constraintType == constraintType }
        if (c is BooleanBoConstraint) first<ZkCheckBox>().checked = c.value
    }

}

class ConstraintDoubleEditor(
    private val constraintType: BoConstraintType
) : SingleConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + benderStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css benderStyles.editorLabel
            + ZkTextInput() css benderStyles.mediumInput
        }
    }

    override fun toBoConstraint(): BoConstraint? {
        val value = first<ZkTextInput>().value.toDoubleOrNull()
        return if (value == null) null else DoubleBoConstraint(constraintType, value)
    }

    override fun update(constraints: List<BoConstraint>) {
        val c = constraints.firstOrNull { it.constraintType == constraintType }
        if (c is DoubleBoConstraint) first<ZkTextInput>().value = c.value?.toString() ?: ""
    }

}

class ConstraintIntEditor(
    private val constraintType: BoConstraintType
) : SingleConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + benderStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css benderStyles.editorLabel
            + ZkTextInput() css benderStyles.mediumInput
        }
    }

    override fun toBoConstraint(): BoConstraint? {
        val value = first<ZkTextInput>().value.toIntOrNull()
        return if (value == null) null else IntBoConstraint(constraintType, value)
    }

    override fun update(constraints: List<BoConstraint>) {
        val c = constraints.firstOrNull { it.constraintType == constraintType }
        if (c is IntBoConstraint) first<ZkTextInput>().value = c.value?.toString() ?: ""
    }

}

class ConstraintLongEditor(
    private val constraintType: BoConstraintType
) : SingleConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + benderStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css benderStyles.editorLabel
            + ZkTextInput() css benderStyles.mediumInput
        }
    }

    override fun toBoConstraint(): BoConstraint? {
        val value = first<ZkTextInput>().value.toLongOrNull()
        return if (value == null) null else LongBoConstraint(constraintType, value)
    }

    override fun update(constraints: List<BoConstraint>) {
        val c = constraints.firstOrNull { it.constraintType == constraintType }
        if (c is LongBoConstraint) first<ZkTextInput>().value = c.value?.toString() ?: ""
    }


}

class ConstraintStringEditor(
    private val constraintType: BoConstraintType
) : SingleConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + benderStyles.constraintEditor

        + row {
            + div { + "${constraintType.name}:" } css benderStyles.editorLabel
            + ZkTextInput() css benderStyles.mediumInput
        }
    }

    override fun toBoConstraint(): BoConstraint? {
        val value = first<ZkTextInput>().value
        return if (value.isEmpty()) null else StringBoConstraint(constraintType, value)
    }

    override fun update(constraints: List<BoConstraint>) {
        val c = constraints.firstOrNull { it.constraintType == constraintType }
        if (c is StringBoConstraint) first<ZkTextInput>().value = c.value ?: ""
    }


}