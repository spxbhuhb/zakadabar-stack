/*
 * Copyright © 2020, Simplexion, Hungary and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.stack.frontend.builtin.form

import kotlinx.datetime.Instant
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.action.ActionDto
import zakadabar.stack.data.builtin.Secret
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.application.ZkApplication.back
import zakadabar.stack.frontend.builtin.ZkBuiltinStrings.Companion.builtin
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.fields.*
import zakadabar.stack.frontend.builtin.form.structure.ZkFormButtons
import zakadabar.stack.frontend.builtin.form.structure.ZkFormSection
import zakadabar.stack.frontend.builtin.form.structure.ZkInvalidFieldList
import zakadabar.stack.frontend.builtin.toast.ZkToast
import zakadabar.stack.frontend.builtin.toast.toast
import zakadabar.stack.frontend.resources.ZkStringStore.Companion.t
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.log
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

/**
 * Base class for DTO forms.
 *
 * @property  autoLabel  When true labels are automatically added. When false they are not.
 */
open class ZkForm<T : DtoBase> : ZkElement() {

    lateinit var dto: T
    lateinit var mode: ZkFormMode

    /**
     * True when the form is in create mode.
     */
    val isCreate
        get() = (mode == ZkFormMode.Create)

    var title: String = ""
    var titleBar: ZkFormTitleBar? = null

    var autoLabel = true

    @PublicApi
    var fieldGridColumnTemplate: String = "150px 1fr"

    @PublicApi
    var fieldGridRowTemplate: String = "max-content"

    var openUpdate: ((dto: T) -> Unit)? = null

    /**
     * A function to be called when execution of the form operation has a result.
     * This means that the server sent a response which is successful at HTTP level.
     * However, the response may indicate an error.
     */
    var onExecuteResult: ((resultDto: DtoBase) -> Unit)? = null

    var schema = lazy { dto.schema() }

    val fields = mutableListOf<ZkFieldBase<T, *>>()

    /**
     * False until the first time the user clicks on the submit button.
     * Before first submit the list of invalid fields are not shown.
     * After the first submit try the list of invalid fields are shown
     * (if there are any) and is updated automatically.
     */
    var submitTouched = false

    /**
     * The toast that shows the message that there are invalid values.
     * Automatically goes hidden if validation is successful.
     */
    var invalidToast: ZkToast? = null

    /**
     * The submit button element of the form. Set by [ZkFormButtons] which is
     * added by [build] or [buttons]. This is here so we can enable / disable
     * the button depending on the validity of the form.
     */
    var submitButton: ZkElement? = null

    /** An element that is show when when the form is submitting. */
    var progressIndicator: ZkElement? = null

    /**
     * Container to display list of the invalid fields. Filled automatically when
     * present.
     */
    var invalidFields: ZkInvalidFieldList? = null

    /**
     * When true the form will go back one page after create is successful.
     */
    var goBackAfterCreate: Boolean = true

    init {
        classList += ZkFormStyles.outerContainer
    }

    // ----  Builder convenience functions --------

    open fun build(title: String, createTitle: String = title, addButtons: Boolean = true, builder: () -> Unit): ZkForm<T> {
        + titleBar(title, createTitle)

        + div(ZkFormStyles.contentContainer) {
            + column(ZkFormStyles.form) {
                builder()
                if (addButtons) {
                    + buttons()
                }
                + invalidFieldList()
            }
        }

        return this
    }

    /**
     * Creates a title bar, using [title] when not in create mode and
     * [createTitle] when in create mode.
     */
    open fun titleBar(title: String, createTitle: String = title): ZkFormTitleBar {

        val bar = ZkFormTitleBar {
            this.title = if (isCreate) createTitle else title
        }

        titleBar = bar

        return bar
    }

    fun buttons() = ZkFormButtons(this@ZkForm)

    open fun invalidFieldList() = ZkInvalidFieldList().also { invalidFields = it }.hide()

    fun section(title: String? = null, summary: String? = null, fieldGrid: Boolean = true, css: String? = null, builder: ZkElement.() -> Unit): ZkFormSection {
        return if (fieldGrid) {
            ZkFormSection(title, summary, css) { + fieldGrid { builder() } }
        } else {
            ZkFormSection(title, summary, css, builder)
        }
    }

    @Deprecated("use simple section instead", ReplaceWith("section(title, summary, builder)"))
    fun fieldGridSection(title: String, summary: String = "", css: String? = null, builder: ZkElement.() -> Unit) =
        section(title, summary, true, css, builder)

    fun <T : RecordDto<T>> List<T>.by(field: (it: T) -> String) = map { it.id to field(it) }.sortedBy { it.second }

    fun select(kProperty0: KMutableProperty0<RecordId<*>>, sortOptions: Boolean = true, label: String? = null, options: suspend () -> List<Pair<RecordId<*>, String>>): ZkRecordSelectField<T> {
        val field = ZkRecordSelectField(this@ZkForm, kProperty0, sortOptions, options)
        label?.let { field.label = label }
        fields += field
        return field
    }

    fun select(kProperty0: KMutableProperty0<RecordId<*>?>, sortOptions: Boolean = true, label: String? = null, options: suspend () -> List<Pair<RecordId<*>, String>>): ZkOptRecordSelectField<T> {
        val field = ZkOptRecordSelectField(this@ZkForm, kProperty0, sortOptions, options)
        label?.let { field.label = label }
        fields += field
        return field
    }

    fun select(kProperty0: KMutableProperty0<String>, label: String? = null, sortOptions: Boolean = true, options: List<String>): ZkStringSelectField<T> {
        val field = ZkStringSelectField(this@ZkForm, kProperty0, sortOptions, suspend { options.map { Pair(it, it) } })
        label?.let { field.label = label }
        fields += field
        return field
    }

    fun select(kProperty0: KMutableProperty0<String?>, label: String? = null, sortOptions: Boolean = true, options: List<String>): ZkOptStringSelectField<T> {
        val field = ZkOptStringSelectField(this@ZkForm, kProperty0, sortOptions, suspend { options.map { Pair(it, it) } })
        label?.let { field.label = label }
        fields += field
        return field
    }

    inline fun <reified E : Enum<E>> select(kProperty0: KMutableProperty0<E>, label: String? = null, sortOptions: Boolean = true): ZkEnumSelectField<T, E> {
        val options = enumValues<E>().map { it to t(it.name) } // this is a non-translated to translated mapping
        val field = ZkEnumSelectField(this@ZkForm, kProperty0, { enumValueOf(it) }, sortOptions, suspend { options })
        label?.let { field.label = label }
        fields += field
        return field
    }

    @JsName("FormOptEnumSelect")
    inline fun <reified E : Enum<E>> select(kProperty0: KMutableProperty0<E?>, label: String? = null, sortOptions: Boolean = true): ZkOptEnumSelectField<T, E> {
        val options = enumValues<E>().map { it to t(it.name) } // this is a non-translated to translated mapping
        val field = ZkOptEnumSelectField(this@ZkForm, kProperty0, { enumValueOf(it) }, sortOptions, suspend { options })
        label?.let { field.label = label }
        fields += field
        return field
    }

    fun textarea(kProperty0: KMutableProperty0<String>, builder: ZkElement.() -> Unit = { }): ZkTextAreaField<T> {
        val field = ZkTextAreaField(this@ZkForm, kProperty0)
        fields += field
        field.builder()
        return field
    }

    fun textarea(kProperty0: KMutableProperty0<String?>, builder: ZkElement.() -> Unit = { }): ZkOptTextAreaField<T> {
        val field = ZkOptTextAreaField(this@ZkForm, kProperty0)
        fields += field
        field.builder()
        return field
    }

    fun opt(kProperty0: KMutableProperty0<Boolean?>, trueText: String, falseText: String, builder: ZkOptBooleanField<T>.() -> Unit = { }): ZkOptBooleanField<T> {
        val options = listOf(true to trueText, false to falseText)
        val field = ZkOptBooleanField(this@ZkForm, kProperty0) { options }
        fields += field
        field.builder()
        return field
    }

    fun constString(label: String, value: () -> String): ZkConstStringField<T> {
        val field = ZkConstStringField(this@ZkForm, label, value())
        fields += field
        return field
    }

    fun ifNotCreate(build: ZkElement.() -> Unit) {
        if (mode == ZkFormMode.Create) return
        build()
    }

    fun fieldGrid(build: ZkElement.() -> Unit) =
        grid(style = "grid-template-columns: $fieldGridColumnTemplate; gap: 0; grid-auto-rows: $fieldGridRowTemplate", build = build)

    // ----  Customized build and property receiver  --------

    operator fun KMutableProperty0<RecordId<T>>.unaryPlus(): ZkElement {
        val field = ZkIdField(this@ZkForm, this)
        + field
        fields += field
        if (mode == ZkFormMode.Create) {
            field.hide()
        }
        return field
    }

    operator fun KMutableProperty0<String>.unaryPlus(): ZkElement {
        val field = ZkStringField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<String?>.unaryPlus(): ZkElement {
        val field = ZkOptStringField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Int>.unaryPlus(): ZkElement {
        val field = ZkIntField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Double>.unaryPlus(): ZkElement {
        val field = ZkDoubleField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Boolean>.unaryPlus(): ZkElement {
        val field = ZkBooleanField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Instant>.unaryPlus(): ZkElement {
        val field = ZkInstantField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Instant?>.unaryPlus(): ZkElement {
        val field = ZkOptInstantField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Secret>.unaryPlus(): ZkElement {
        val field = ZkSecretField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Secret?>.unaryPlus(): ZkElement {
        val field = ZkOptSecretField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    inline operator fun <reified E : Enum<E>> KMutableProperty0<E>.unaryPlus(): ZkElement {
        val field = select(this)
        + field
        return field
    }

    @JsName("FormOptEnumUnaryPlus")
    inline operator fun <reified E : Enum<E>> KMutableProperty0<E?>.unaryPlus(): ZkElement {
        val field = select(this)
        + field
        return field
    }

    // ----  Validation and submit --------

    /**
     * Validates the DTO with the schema. Validation occurs at every
     * change. When the user did not touch a field yet a validation
     * fail may not be displayed for that field.
     *
     * @return true if the DTO is valid, false otherwise
     */
    fun validate(submit: Boolean = false): Boolean {
        if (submit) {
            // submit marks all fields touched to show all invalid fields for the user
            fields.forEach {
                it.touched = true
            }
            submitTouched = true
        }

        val report = schema.value.validate()

        fields.forEach {
            it.onValidated(report)
        }

        val invalid = invalidTouchedFields(report)

        if (! submitTouched || invalid.isEmpty()) {
            invalidFields?.hide()
            invalidToast?.dispose()
        }

        if (submitTouched && invalid.isNotEmpty()) {
            invalidFields?.show(invalid)
        }

        if (submit && invalid.isNotEmpty()) {
            onInvalidSubmit()
        }

        return report.fails.isEmpty()
    }

    private fun invalidTouchedFields(report: ValidityReport): List<ZkFieldBase<T, *>> {
        val invalid = mutableListOf<ZkFieldBase<T, *>>()

        report.fails.keys.forEach { propName ->
            val field = fields.firstOrNull { it.propName == propName } ?: return@forEach
            if (field.touched) {
                invalid += field
            }
        }

        // it is possible that a field is not touched but invalid by default
        // for example a mandatory name which cannot be blank but starts with blank

        fields.forEach {
            if (it.touched && ! it.valid) invalid += it
        }

        // using distinct because report and field.invalid can add the same field twice

        return invalid.distinct()
    }

    /**
     * Callback to finalize the DTO before submit starts. Called before validation.
     * Default implementation does nothing.
     */
    open fun onSubmitStart() {

    }

    fun submit() {

        onSubmitStart()

        if (! validate(true)) return

        io {
            try {
                submitButton?.hide()
                progressIndicator?.show()

                when (mode) {
                    ZkFormMode.Create -> {
                        val created = (dto as RecordDto<*>).create() as RecordDto<*>
                        fields.forEach { it.onCreateSuccess(created) }
                        if (goBackAfterCreate) {
                            back()
                        } else {
                            resetTouched()
                        }
                    }
                    ZkFormMode.Read -> {
                        // nothing to do here
                    }
                    ZkFormMode.Update -> {
                        (dto as RecordDto<*>).update()
                        resetTouched()
                    }
                    ZkFormMode.Delete -> {
                        (dto as RecordDto<*>).delete()
                        resetTouched()
                    }
                    ZkFormMode.Action -> {
                        val result = (dto as ActionDto<*>).execute()
                        onExecuteResult?.let { it(result) }
                        resetTouched()
                    }
                    ZkFormMode.Query -> {
                        (dto as QueryDto<*>).execute()
                        // TODO do something here with the result
                    }
                }

                onSubmitSuccess()

            } catch (ex: Exception) {
                log(ex)
                onSubmitError(ex)
            } finally {
                submitButton?.show()
                progressIndicator?.hide()
            }
        }
    }

    private fun resetTouched() {
        submitTouched = false
        fields.forEach {
            it.touched = false
        }
    }

    /**
     * Called when the user tries to submit an invalid form.
     *
     * Default implementation shows a toast with a message.
     */
    open fun onInvalidSubmit() {
        invalidToast = toast(warning = true, hideAfter = 3000) { builtin.invalidFieldsToast }
    }

    /**
     * Called when submitting the form returns with a success.
     *
     * Default implementation shows a toast.
     */
    open fun onSubmitSuccess() {
        when (mode) {
            ZkFormMode.Create -> toast { builtin.createSuccess }
            ZkFormMode.Read -> Unit
            ZkFormMode.Update -> toast { builtin.updateSuccess }
            ZkFormMode.Delete -> toast { builtin.deleteSuccess }
            ZkFormMode.Action -> toast { builtin.actionSuccess }
            ZkFormMode.Query -> Unit
        }
    }

    /**
     * Called when submitting the form returns with an error.
     *
     * Default implementation shows a toast.
     */
    open fun onSubmitError(ex: Exception) {
        when (mode) {
            ZkFormMode.Create -> toast(error = true) { builtin.createFail }
            ZkFormMode.Read -> Unit
            ZkFormMode.Update -> toast(error = true) { builtin.updateFail }
            ZkFormMode.Delete -> toast(error = true) { builtin.deleteFail }
            ZkFormMode.Action -> toast(error = true) { builtin.actionFail }
            ZkFormMode.Query -> toast(error = true) { builtin.queryFail }
        }
    }

}