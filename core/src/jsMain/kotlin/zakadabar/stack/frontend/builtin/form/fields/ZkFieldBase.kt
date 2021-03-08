/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.fields

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkBuiltinStrings.Companion.builtin
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign

abstract class ZkFieldBase<FT : DtoBase, DT>(
    val form: ZkForm<FT>,
    val propName: String
) : ZkElement() {

    var readOnly = false

    var touched = false

    var valid = true

    /**
     * Label of the form field. Displayed in front of the field, used in error messages.
     */
    open lateinit var label: String

    lateinit var hint: String

    val fieldBottomBorder = document.createElement("div") as HTMLElement

    val errors = ZkElement().css(ZkFormStyles.fieldError)

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

        fieldBottomBorder.className = ZkFormStyles.fieldBottomBorder
        + fieldBottomBorder

        on("mouseenter") { _ ->
            fieldBottomBorder.classList += ZkFormStyles.onFieldHover
        }

        on("mouseleave") { _ ->
            fieldBottomBorder.classList -= ZkFormStyles.onFieldHover
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
     * looks up the label in [ZkApplication.stringMap] and adds
     * it if found.
     *
     * When not in [ZkApplication.stringMap] it adds [propName]
     * as label.
     */
    open fun buildFieldLabel() {
        if (! ::label.isInitialized) {
            label = ZkApplication.stringStore.map[propName] ?: propName
        }

        + div(ZkFormStyles.fieldLabel) {
            + label
            mandatoryMark()
            on(buildElement, "click") { _ -> focusValue() }
        }
    }

    open fun mandatoryMark() {
        if (! form.schema.value.isOptional(propName)) {
            + div(ZkFormStyles.mandatoryMark) { ! "&nbsp;*" }
        }
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
        if (fails == null) {
            updateValid(true)
            errors.hide()
        } else {
            updateValid(false)
            showErrors()
        }
    }

    open fun updateValid(valid: Boolean) {
        this.valid = valid
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
        errors.innerHTML = builtin.invalidValue
        errors.show()
    }

    /**
     * Called after a successful form submit in create mode.
     */
    open suspend fun onCreateSuccess(created: RecordDto<*>) = Unit
}