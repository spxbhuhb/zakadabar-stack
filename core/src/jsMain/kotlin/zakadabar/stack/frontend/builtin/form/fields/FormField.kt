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
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.minusAssign
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.CoreStrings

abstract class FormField<FT : DtoBase, DT>(
    val form: ZkForm<FT>,
    val propName: String
) : ZkElement() {

    var readOnly = false

    var touched = false

    var valid = true

    lateinit var label: String

    lateinit var hint: String

    val fieldBottomBorder = document.createElement("div") as HTMLElement

    val errors = ZkElement().withClass(ZkFormStyles.fieldError)

    override fun init(): ZkElement {
        buildSectionField()
        return this
    }

    /**
     * Builds the default layout for a field. Calls [buildFieldValue]
     * to build the actual field value.
     *
     * Call this function from [init] or simply write your own implementation.
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
     * Builds the label for the field. First it checks if the label
     * property is initialized. If so adds its value as the label.
     *
     * If it is not initialized, but form.autoLabel is true looks
     * up the label in Application.stringMap and adds it if found.
     */
    open fun buildFieldLabel() {
        if (::label.isInitialized) {
            + div(ZkFormStyles.fieldLabel) {
                + label
            }
            return
        }
        if (form.autoLabel) {
            + div(ZkFormStyles.fieldLabel) {
                + (Application.stringMap[propName] ?: propName)
                if (! form.schema.value.isOptional(propName)) {
                    + div(ZkFormStyles.mandatoryMark) { ! "&nbsp;*" }
                }
            }
        }
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
        errors.innerHTML = CoreStrings.invalidValue
        errors.show()
    }

    /**
     * Called after a successful form submit in create mode.
     */
    open suspend fun onCreateSuccess(created: RecordDto<*>) = Unit
}