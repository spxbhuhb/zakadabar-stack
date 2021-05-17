/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkCheckBox
import zakadabar.stack.frontend.builtin.input.ZkTextInput

open class ConstraintEditor : ZkElement() {
    open fun toDto(): ConstraintDto? {
        TODO()
    }
}

class Optional : ZkElement() {
    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + div { + "Optional" } css kodomatStyles.editorLabel
        + ZkCheckBox()
    }

    val value: Boolean
        get() = findFirst<ZkCheckBox>().checked
}

class ConstraintBooleanEditor(
    private val constraintType: ConstraintType,
    private val skipWhen: Boolean
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + div { + constraintType.name } css kodomatStyles.editorLabel
        + ZkCheckBox(checked = skipWhen)
    }

    override fun toDto(): ConstraintDto? {
        val value = findFirst<ZkCheckBox>().checked
        return if (value == skipWhen) null else ConstraintBooleanDto(constraintType, value)
    }
}

class ConstraintDoubleEditor(
    private val constraintType: ConstraintType
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + div { + constraintType.name } css kodomatStyles.editorLabel
        + ZkTextInput() css kodomatStyles.mediumInput
    }

    override fun toDto(): ConstraintDto? {
        val value = findFirst<ZkTextInput>().value.toDoubleOrNull()
        return if (value == null) null else ConstraintDoubleDto(constraintType, value)
    }

}

class ConstraintIntEditor(
    private val constraintType: ConstraintType
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + div { + constraintType.name } css kodomatStyles.editorLabel
        + ZkTextInput() css kodomatStyles.mediumInput
    }

    override fun toDto(): ConstraintDto? {
        val value = findFirst<ZkTextInput>().value.toIntOrNull()
        return if (value == null) null else ConstraintIntDto(constraintType, value)
    }

}

class ConstraintLongEditor(
    private val constraintType: ConstraintType
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + div { + constraintType.name } css kodomatStyles.editorLabel
        + ZkTextInput() css kodomatStyles.mediumInput
    }

    override fun toDto(): ConstraintDto? {
        val value = findFirst<ZkTextInput>().value.toLongOrNull()
        return if (value == null) null else ConstraintLongDto(constraintType, value)
    }

}

class ConstraintStringEditor(
    private val constraintType: ConstraintType
) : ConstraintEditor() {

    override fun onCreate() {
        super.onCreate()

        + kodomatStyles.constraintEditor

        + div { + constraintType.name } css kodomatStyles.editorLabel
        + ZkTextInput() css kodomatStyles.mediumInput
    }

    override fun toDto(): ConstraintDto? {
        val value = findFirst<ZkTextInput>().value
        return if (value.isEmpty()) null else ConstraintStringDto(constraintType, value)
    }

}