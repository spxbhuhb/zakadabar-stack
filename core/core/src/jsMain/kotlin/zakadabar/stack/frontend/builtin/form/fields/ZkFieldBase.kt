/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.form.fields

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.schema.ValidityReport
import zakadabar.core.data.schema.descriptor.BoConstraintType
import zakadabar.core.data.schema.descriptor.BooleanBoConstraint
import zakadabar.core.data.schema.descriptor.IntBoConstraint
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.form.ZkFormStyles
import zakadabar.core.frontend.builtin.form.zkFormStyles
import zakadabar.core.frontend.util.minusAssign
import zakadabar.core.frontend.util.plusAssign
import zakadabar.core.resources.localizedStrings

abstract class ZkFieldBase<DT>(
    val context: ZkFieldContext,
    val propName: String,
    label: String? = null
) : ZkElement() {

    abstract var readOnly: Boolean

    var touched = false

    open val labelContainer = document.createElement("div") as HTMLElement

    @Deprecated("EOL: 2021.8.1  -  use labelText instead", ReplaceWith("labelText"))
    open var label
        get() = labelText
        set(value) {
            labelText = value
        }

    open var labelText: String? = label
        set(value) {
            field = value
            labelContainer.innerText = value ?: ""
        }

    /**
     * True when the input value is invalid itself, without the schema. This
     * is the case for mandatory enumerations for example when the bo has
     * to store a value as it cannot be null. However, if the user selects
     * "not selected", the input is invalid, but the schema validation won't
     * show it up.
     */
    var invalidInput = false

    var valid: Boolean = true
        set(value) {
            field = value
            setValidClass()
        }

    lateinit var hint: String

    private val errors = ZkElement().css(ZkFormStyles.fieldError)

    override fun onCreate() {
        buildSectionField()
    }

    /**
     * Builds the default layout for a field. Calls [buildFieldValue]
     * to build the actual field value.
     *
     * Call this function from [onResume] or simply write your own implementation.
     */
    open fun buildSectionField() {
        classList += zkFormStyles.fieldContainer

        if (context.addLabel) {
            buildFieldLabel()
        }

        + div(if (context.dense) zkFormStyles.fieldValueDense else zkFormStyles.fieldValue) {
            buildFieldValue()
        }

//        if (::hint.isInitialized) {
//            + row(ZkFormStyles.fieldHint) {
//                + hint
//            }
//        }
//
//        + errors
    }

    /**
     * Builds the label for the field.
     *
     * If the [label] property is initialized uses its value.
     *
     * When it is not initialized and form.autoLabel is true,
     * looks up the label in [stringStore] and adds
     * it if found.
     *
     * When not in [stringStore] it adds [propName]
     * as label.
     */
    open fun buildFieldLabel() {
        if (labelText == null) {
            labelText = localizedStrings.getNormalized(propName)
        } else {
            labelText = labelText // to initialize label container
        }

        + div(ZkFormStyles.fieldLabel) {
            + labelContainer
            mandatoryMark()
            on(buildPoint, "click") { focusValue() }
        }
    }

    open fun needsMandatoryMark() = ! context.schema.isOptional(propName)

    open fun mandatoryMark() {
        if (needsMandatoryMark()) {
            + div(ZkFormStyles.mandatoryMark) { ! "&nbsp;*" }
        }
    }
    
    open fun focusEvents(element: HTMLElement) {
        on(element, "blur") {
            context.validate()
        }
    }

    override fun focus(): ZkElement {
        focusValue()
        return this
    }

    /**
     * Focus on the value field.
     */
    open fun focusValue() {

    }

    /**
     * Build the actual field value HTML structure. Called by
     * [buildSectionField].
     */
    open fun buildFieldValue() {

    }

    open fun onValidated(report: ValidityReport) {
        val fails = report.fails[propName]
        if (fails == null && ! invalidInput) {
            valid = true
            errors.hide()
        } else {
            valid = false
            showErrors()
        }
    }

    open fun setValidClass() {
        if (valid) {
            element.removeInvalid()
        } else {
            if (touched) element.addInvalid()
        }
    }

    open fun HTMLElement.addInvalid() {
        classList += "invalid"
        for (i in 0..childElementCount) {
            val child = children[i]
            if (child is HTMLElement) {
                child.addInvalid()
            }
        }
    }

    open fun HTMLElement.removeInvalid() {
        classList -= "invalid"
        for (i in 0..childElementCount) {
            val child = children[i]
            if (child is HTMLElement) {
                child.removeInvalid()
            }
        }
    }

    open fun showErrors() {
        errors.clearChildren() // to clean up previous errors
        // TODO add the actual errors
        errors.innerHTML = localizedStrings.invalidValue
        errors.show()
    }

    /**
     * Called after a successful form submit in create mode.
     */
    open suspend fun onCreateSuccess(created: EntityBo<*>) = Unit

    /**
     * Checks if there are constraints that actually require a value. If there is no such
     * constraint, we should not mark this field mandatory as it might confuse the user.
     *
     * To use this function, override [needsMandatoryMark] and call this from there.
     */
    fun stringMandatoryMark(): Boolean {

        val constraints = context.schema.constraints(propName)

        var mandatory = false
        constraints.forEach {
            when {
                (it.constraintType == BoConstraintType.Min && it is IntBoConstraint && it.value != 0) -> mandatory = true
                (it.constraintType == BoConstraintType.Blank && it is BooleanBoConstraint && ! it.value) -> mandatory = true
            }
        }

        return mandatory
    }

}