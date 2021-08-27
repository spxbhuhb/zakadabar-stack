/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.data.EntityBo
import zakadabar.core.resource.localizedStrings
import zakadabar.core.schema.ValidityReport
import zakadabar.core.schema.descriptor.BoConstraintType
import zakadabar.core.schema.descriptor.BooleanBoConstraint
import zakadabar.core.schema.descriptor.IntBoConstraint

abstract class ZkFieldBase<DT, FT : ZkFieldBase<DT, FT>>(
    val context: ZkFieldContext,
    val propName: String?,
    label: String? = null
) : ZkElement() {

    abstract var readOnly: Boolean

    /**
     * Function to execute then the value changes.
     *
     * @param origin origin of the change
     * @param value the new value
     * @param field the field that changed
     */
    var onChangeCallback: ((origin: ChangeOrigin, value: DT, field: FT) -> Unit)? = null

    /**
     * The UI value of the field or null when it is not set or [invalidInput] is true.
     * The setter of this property has to perform null checks when necessary.
     */
    abstract var valueOrNull: DT?
        protected set

    /**
     * Value of the field. Assigning to this property:
     *
     * - changes the value in the BO (except constant fields)
     * - updates the UI
     * - calls [ZkFieldContext.validate]
     * - calls [onChangeCallback] with [ChangeOrigin.Code]
     *
     * @throws IllegalStateException When [invalidInput] is true.
     * @throws NoSuchElementException When the field is not set.
     */
    open var value: DT
        get() {
            if (invalidInput) throw IllegalStateException()
            return valueOrNull ?: throw NoSuchElementException()
        }
        set(value) {
            valueOrNull = value
            context.validate()
            @Suppress("UNCHECKED_CAST")
            onChangeCallback?.invoke(ChangeOrigin.Code, value, this as FT)
        }

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

    private val errors = ZkElement().css(context.styles.fieldError)

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
        classList += context.styles.fieldContainer

        if (context.addLabel) {
            buildFieldLabel()
        }

        + div(context.styles.fieldValue) {
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
            labelText = propName?.let { localizedStrings.getNormalized(it) } ?: ""
        } else {
            labelText = labelText // to initialize label container
        }

        + div(context.styles.fieldLabel) {
            + labelContainer
            mandatoryMark()
            on(buildPoint, "click") { focusValue() }
        }
    }

    open fun needsMandatoryMark() = propName?.let { ! context.schema.isOptional(propName) } ?: false

    open fun mandatoryMark() {
        if (needsMandatoryMark()) {
            + div(context.styles.mandatoryMark) { ! "&nbsp;*" }
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

    /**
     * The field calls this method whenever the user changes the value of
     * the field. Default implementation:
     *
     * - sets [touched] to true
     * - calls [ZkFieldContext.validate]
     * - calls [onChangeCallback] with [ChangeOrigin.User]
     */
    open fun onUserChange(newValue: DT) {
        touched = true
        context.validate()
        @Suppress("UNCHECKED_CAST")
        onChangeCallback?.invoke(ChangeOrigin.User, newValue, this as FT)
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

        if (propName == null) return false

        val constraints = context.schema.constraintsOrNull(propName) ?: return false

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