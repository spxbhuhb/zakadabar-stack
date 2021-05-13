/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.fields

import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign

abstract class ZkFieldBase<FT : DtoBase, DT>(
    val form: ZkForm<FT>,
    val propName: String,
    open var label: String? = null,
    open var readOnly: Boolean = (form.mode == ZkElementMode.Read)
) : ZkElement() {

    var touched = false

    /**
     * True when the input value is invalid itself, without the schema. This
     * is the case for mandatory enumerations for example when the dto has
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
        classList += ZkFormStyles.fieldContainer

        buildFieldLabel()

        + div(ZkFormStyles.fieldValue) {
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
        if (label == null) {
            label = stringStore.map[propName] ?: propName
        }

        + div(ZkFormStyles.fieldLabel) {
            + label
            mandatoryMark()
            on(buildPoint, "click") { focusValue() }
        }
    }

    open fun mandatoryMark() {
        if (! form.schema.value.isOptional(propName)) {
            + div(ZkFormStyles.mandatoryMark) { ! "&nbsp;*" }
        }
    }

    open fun focusEvents(element: HTMLElement) {
        on(element, "blur") {
            form.validate()
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
        errors.innerHTML = stringStore.invalidValue
        errors.show()
    }

    /**
     * Called after a successful form submit in create mode.
     */
    open suspend fun onCreateSuccess(created: RecordDto<*>) = Unit
}